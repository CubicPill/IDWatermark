package me.cubicpill.idwatermark

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2
    private val REQUEST_EDITOR = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val galleryButton = findViewById<Button>(R.id.galleryBtn)
        val cameraButton = findViewById<Button>(R.id.cameraBtn)
        val clickListener = View.OnClickListener { view ->

            when (view.id) {
                R.id.galleryBtn -> onGalleryButtonClick()
                R.id.cameraBtn -> onCameraButtonClick()
            }
        }
        galleryButton.setOnClickListener(clickListener)
        cameraButton.setOnClickListener(clickListener)
    }

    fun onCameraButtonClick() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }
    }

    fun onGalleryButtonClick() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)

    }

}
