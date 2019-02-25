package me.cubicpill.idwatermark

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val galleryButton = findViewById<Button>(R.id.galleryBtn)
        val cameraButton = findViewById<Button>(R.id.cameraBtn)
        
    }

    fun onCameraButtonClick() {

    }

    fun onGalleryButtonClick() {

    }


}
