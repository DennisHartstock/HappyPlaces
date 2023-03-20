package com.commcode.happyplaces.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.commcode.happyplaces.database.DatabaseHandler
import com.commcode.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.commcode.happyplaces.models.HappyPlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), MultiplePermissionsListener {

    private lateinit var binding: ActivityAddHappyPlaceBinding
    private var calendar = Calendar.getInstance()
    private var locationImage: Uri? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0

    companion object {
        private const val GALLERY = 1
        private const val CAMERA_REQUEST_CODE = 2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivImage.clipToOutline = true

        binding.etDate.setOnClickListener { getDate() }

        binding.tvAddImage.setOnClickListener {
            val addPictureDialog = AlertDialog.Builder(this)
            val addPictureDialogItems =
                arrayOf("Select photo from gallery", "Capture photo from camera")
            addPictureDialog
                .setTitle("Select action")
                .setItems(addPictureDialogItems) { _, which ->
                    when (which) {
                        0 -> {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                choosePhotoFromGallery()
                            } else {
                                checkPermissions()
                            }
                        }
                        1 -> {
                            if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                capturePhoto()
                            } else {
                                checkPermissions()
                            }
                        }
                    }
                }
                .show()
        }

        binding.btnSave.setOnClickListener {
            when {
                binding.etTitle.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter the title", Toast.LENGTH_SHORT).show()
                }
                binding.etDescription.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter the description", Toast.LENGTH_SHORT).show()
                }
                binding.etDate.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please select the date", Toast.LENGTH_SHORT).show()
                }
                binding.etLocation.text.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please enter the location", Toast.LENGTH_SHORT).show()
                }
                locationImage == null -> {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val happyPlaceModel = HappyPlaceModel(
                        0,
                        binding.etTitle.text.toString(),
                        locationImage.toString(),
                        binding.etDescription.text.toString(),
                        binding.etDate.text.toString(),
                        binding.etLocation.text.toString(),
                        mLatitude,
                        mLongitude
                    )
                    val dbHandler = DatabaseHandler(this)
                    val addHappyPlace = dbHandler.addHappyPlace(happyPlaceModel)
                    if (addHappyPlace > 0) {
                        Toast.makeText(
                            this,
                            "The happy place details inserted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentUri = data.data
                    try {
                        val selectedBitmap =
                            MediaStore.Images.Media.getBitmap(contentResolver, contentUri)
                        locationImage = saveImageToInternalStorage(selectedBitmap)
                        binding.ivImage.setImageBitmap(selectedBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Failed to load the image from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                locationImage = saveImageToInternalStorage(thumbnail)
                binding.ivImage.setImageBitmap(thumbnail)
            }
        }
    }

    private fun checkPermissions() {
        val storagePermissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        Dexter
            .withContext(this)
            .withPermissions(storagePermissions)
            .withListener(this)
            .onSameThread()
            .check()
    }

    private fun getDate() {
        DatePickerDialog(
            this@AddHappyPlaceActivity,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val dateFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val date = sdf.format(calendar.time).toString()
        (binding.etDate as TextView).text = date
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        if (report!!.areAllPermissionsGranted()) {
            choosePhotoFromGallery()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: MutableList<PermissionRequest>?,
        token: PermissionToken?,
    ) {
        showRationalDialogForPermissions()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage(
                "It looks like you have turned off Storage or Camera Permissions required " +
                        "for this feature. It can be enabled under the Application Settings"
            )
            .setPositiveButton("go to application settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}