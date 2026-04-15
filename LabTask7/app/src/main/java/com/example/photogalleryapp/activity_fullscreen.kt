package com.example.photogalleryapp

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class FullscreenActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val matrix = Matrix()

    // Pinch-to-zoom state
    private var lastDistance = 0f
    private var scaleFactor = 1f
    private val minScale = 0.5f
    private val maxScale = 5f

    // Pan state
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isDragging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        imageView = findViewById(R.id.fullscreenImage)
        val titleText: TextView = findViewById(R.id.fullscreenTitle)

        val resourceId = intent.getIntExtra("resourceId", 0)
        val title      = intent.getStringExtra("title") ?: ""

        if (resourceId != 0) imageView.setImageResource(resourceId)
        titleText.text = title

        imageView.scaleType = ImageView.ScaleType.MATRIX

        imageView.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }
    }

    private fun handleTouch(event: MotionEvent) {
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                isDragging = true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                lastDistance = getDistance(event)
                isDragging = false
            }

            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2) {
                    // Pinch zoom
                    val newDist = getDistance(event)
                    if (lastDistance > 0) {
                        val scale = newDist / lastDistance
                        val newScale = scaleFactor * scale
                        if (newScale in minScale..maxScale) {
                            scaleFactor = newScale
                            val cx = (event.getX(0) + event.getX(1)) / 2
                            val cy = (event.getY(0) + event.getY(1)) / 2
                            matrix.postScale(scale, scale, cx, cy)
                            imageView.imageMatrix = matrix
                        }
                    }
                    lastDistance = newDist
                } else if (event.pointerCount == 1 && isDragging) {
                    // Pan
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    matrix.postTranslate(dx, dy)
                    imageView.imageMatrix = matrix
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                lastDistance = 0f
                isDragging = false
            }
        }
    }

    private fun getDistance(event: MotionEvent): Float {
        val dx = event.getX(0) - event.getX(1)
        val dy = event.getY(0) - event.getY(1)
        return sqrt(dx * dx + dy * dy)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Center image in the view once dimensions are known
            val drawable = imageView.drawable ?: return
            val vw = imageView.width.toFloat()
            val vh = imageView.height.toFloat()
            val iw = drawable.intrinsicWidth.toFloat()
            val ih = drawable.intrinsicHeight.toFloat()
            val scale = minOf(vw / iw, vh / ih)
            val tx = (vw - iw * scale) / 2f
            val ty = (vh - ih * scale) / 2f
            matrix.reset()
            matrix.postScale(scale, scale)
            matrix.postTranslate(tx, ty)
            scaleFactor = scale
            imageView.imageMatrix = matrix
        }
    }
}