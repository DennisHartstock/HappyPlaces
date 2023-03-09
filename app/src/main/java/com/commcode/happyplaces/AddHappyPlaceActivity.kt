package com.commcode.happyplaces

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
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
import com.commcode.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), MultiplePermissionsListener {

    private lateinit var binding: ActivityAddHappyPlaceBinding
    private var calendar = Calendar.getInstance()

    companion object {
        private const val CAMERA_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
    }

    private fun choosePhotoFromGallery() {
        TODO("Not yet implemented")
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
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
            Toast.makeText(
                this@AddHappyPlaceActivity,
                "Permissions are granted",
                Toast.LENGTH_SHORT
            ).show()
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