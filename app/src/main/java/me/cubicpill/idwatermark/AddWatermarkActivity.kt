package me.cubicpill.idwatermark

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_watermark.*

import android.graphics.Color
import com.watermark.androidwm.WatermarkBuilder
import com.watermark.androidwm.bean.WatermarkText
import android.text.Editable
import android.text.TextWatcher
import com.divyanshu.colorseekbar.ColorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.SeekParams


class AddWatermarkActivity : AppCompatActivity() {
    var positionX = 0.5
    var positionY = 0.5
    var textColor = Color.WHITE
    var textAlpha = 255
    var textSize = 20.0
    var textRotation = 0.0
    var tileMode = true
    var grayMode = false
    lateinit var imageBitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_watermark)

        val bundle = intent.extras
        val imageUri: Uri = bundle!!.get("IMAGE_URI") as Uri
        Log.d("IDWatermark", "Get URI: $imageUri")
        imageBitmap = BitmapFactory.decodeFile(imageUri.encodedPath)

        watermarkImageView.setImageURI(imageUri)
        rotationSeekBar.setIndicatorTextFormat("\${PROGRESS}°")
        rotationSeekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(p: SeekParams) {
                textRotation = p.progressFloat.toDouble()
                drawWatermarkOnImage()
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {

            }
        }


        alphaSeekBar.onSeekChangeListener = object : OnSeekChangeListener {

            override fun onSeeking(p: SeekParams) {
                textAlpha = p.progress
                drawWatermarkOnImage()
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {

            }
        }
        sizeSeekBar.onSeekChangeListener = object : OnSeekChangeListener {

            override fun onSeeking(p: SeekParams) {
                textSize = p.progress.toDouble()
                drawWatermarkOnImage()
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {

            }
        }
        watermarkText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                drawWatermarkOnImage()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        colorSeekBar.setOnColorChangeListener(object : ColorSeekBar.OnColorChangeListener {
            override fun onColorChangeListener(color: Int) {
                textColor = color
                drawWatermarkOnImage()
            }
        })

        tileModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            run {
                tileMode = isChecked
                drawWatermarkOnImage()
            }

        }
        grayModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            run {
                grayMode = isChecked
                drawWatermarkOnImage()
            }

        }

    }

    fun drawWatermarkOnImage() {
        val watermarkText = WatermarkText(watermarkText.text.toString())
                .setPositionX(positionX)
                .setPositionY(positionY)
                .setTextColor(textColor)
                //.setTextShadow(0.1f, 5f, 5f, Color.BLUE)
                .setTextAlpha(textAlpha)

                .setRotation(textRotation)
                .setTextSize(textSize)

        WatermarkBuilder
                .create(this, imageBitmap)
                .loadWatermarkText(watermarkText) // use .loadWatermarkImage(watermarkImage) to load an image.
                .setTileMode(tileMode)
                .getWatermark()
                .setToImageView(watermarkImageView)
    }
}

