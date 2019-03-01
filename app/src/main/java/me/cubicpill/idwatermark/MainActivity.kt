package me.cubicpill.idwatermark

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2
    private var currentPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickListener = View.OnClickListener { view ->

            when (view.id) {
                R.id.galleryBtn -> onGalleryButtonClick()
                R.id.cameraBtn -> onCameraButtonClick()
            }
        }
        galleryBtn.setOnClickListener(clickListener)
        cameraBtn.setOnClickListener(clickListener)

    }

    private fun onCameraButtonClick() {
        val cameraPhoto = File.createTempFile("ID_WM", null, this.cacheDir)
        Log.d("IDWatermark", "cameraPhoto uri " + cameraPhoto.toURI().toString())
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoUri = FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                cameraPhoto)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        currentPhotoUri = photoUri
        if (cameraIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }
    }

    private fun onGalleryButtonClick() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("IDWatermark", "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            Log.d("IDWatermark", "Result: OK!")
            when (requestCode) {
                REQUEST_CAMERA -> {
                    Log.d("IDWatermark", "URI from camera: " + currentPhotoUri.toString())
                    CropImage.activity(currentPhotoUri)
                            .setInitialCropWindowPaddingRatio(0f)
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .start(this)

                }
                REQUEST_GALLERY -> {

                    Log.d("IDWatermark", "URI from gallery " + data?.data.toString())
                    currentPhotoUri = data?.data
                    CropImage.activity(currentPhotoUri)
                            .setInitialCropWindowPaddingRatio(0f)
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .start(this)
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d("IDWatermark", "CropImage result uri: $resultUri")
                    currentPhotoUri = resultUri
                    val addWatermarkIntent = Intent(this, AddWatermarkActivity::class.java)
                    addWatermarkIntent.putExtra("IMAGE_URI", currentPhotoUri)
                    startActivity(addWatermarkIntent)
                }
            }
        }

    }

}
