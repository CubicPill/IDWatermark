package me.cubicpill.idwatermark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button

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
        val loadCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (loadCameraIntent.resolveActivity(packageManager) != null) {

            startActivityForResult(loadCameraIntent, 1)
        }
    }

    fun onGalleryButtonClick() {

    }


}
