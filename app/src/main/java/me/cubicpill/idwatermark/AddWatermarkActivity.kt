package me.cubicpill.idwatermark

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.divyanshu.colorseekbar.ColorSeekBar
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import com.watermark.androidwm.Watermark
import com.watermark.androidwm.WatermarkBuilder
import com.watermark.androidwm.bean.WatermarkText
import kotlinx.android.synthetic.main.activity_add_watermark.*
import java.io.File
import java.io.FileOutputStream


class AddWatermarkActivity : AppCompatActivity() {
    private var positionX = 0.0
    private var positionY = 0.0
    private var textColor = Color.WHITE
    private var textAlpha = 255
    private var textSize = 20.0
    private var textRotation = 0.0
    private var tileMode = true
    private var grayMode = false
    private lateinit var imageBitmap: Bitmap
    private lateinit var watermarkedImage: Watermark
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_watermark)

        val bundle = intent.extras
        val imageUri: Uri = bundle!!.get("IMAGE_URI") as Uri
        Log.d("IDWatermark", "Get URI: $imageUri")
        imageBitmap = BitmapFactory.decodeFile(imageUri.encodedPath)

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

        val clickListener = View.OnClickListener { view ->

            when (view.id) {
                R.id.resetBtn -> reset()
                R.id.doneBtn -> saveImage()
            }
        }
        doneBtn.setOnClickListener(clickListener)
        resetBtn.setOnClickListener(clickListener)

        initWatermarkView()

    }

    private fun initWatermarkView() {
        //textColor = colorSeekBar.getColor()
        textAlpha = alphaSeekBar.progress
        textRotation = rotationSeekBar.progress.toDouble()
        textSize = sizeSeekBar.progress.toDouble()
        tileMode = tileModeSwitch.isChecked
        grayMode = grayModeSwitch.isChecked
        drawWatermarkOnImage()

    }

    private fun drawWatermarkOnImage() {
        val watermarkText = WatermarkText(watermarkText.text.toString())
                .setPositionX(positionX)
                .setPositionY(positionY)
                .setTextColor(textColor)
                //.setTextShadow(0.1f, 5f, 5f, Color.BLUE)
                .setTextAlpha(textAlpha)

                .setRotation(textRotation)
                .setTextSize(textSize)

        watermarkedImage = WatermarkBuilder
                .create(this, imageBitmap)
                .loadWatermarkText(watermarkText)
                .setTileMode(tileMode)
                .watermark
        watermarkedImage.setToImageView(watermarkImageView)
    }

    private fun reset() {
        drawWatermarkOnImage()
    }

    private fun saveImage() {
        val bitmap = watermarkedImage
                .outputImage
        val picPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).absolutePath
        val image = File(picPath, "watermark.png")
        Log.d("IDM", "Saving to ${image.absolutePath}")
        val out = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
    }
}

