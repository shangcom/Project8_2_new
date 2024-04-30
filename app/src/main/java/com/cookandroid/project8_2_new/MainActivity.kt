package com.cookandroid.project8_2_new

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var myPicture: myPictureView
    private var curNum: Int = 0
    private var imageFiles: Array<File>? = null
    private lateinit var imageFname: String

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                loadImages()
            } else {
                Toast.makeText(this, "External storage permission required", Toast.LENGTH_LONG)
                    .show()
                finish()  // Close the app if permission is not granted
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "간단 이미지 뷰어"

        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        myPicture = findViewById(R.id.myPictureView1)

        checkPermissionsAndLoadImages()

        btnPrev.setOnClickListener {
            if (curNum <= 0) {
                Toast.makeText(applicationContext, "첫번째 그림입니다", Toast.LENGTH_SHORT).show()
            } else {
                curNum--
                updateImage()
            }
        }

        btnNext.setOnClickListener {
            if (imageFiles != null && curNum >= imageFiles!!.size - 1) {
                Toast.makeText(applicationContext, "마지막 그림입니다", Toast.LENGTH_SHORT).show()
            } else {
                curNum++
                updateImage()
            }
        }
    }

    private fun checkPermissionsAndLoadImages() {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                loadImages()
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun loadImages() {
        val picturesDirectory =
            File(Environment.getExternalStorageDirectory().absolutePath + "/Pictures")
        imageFiles = picturesDirectory.listFiles()
        if (imageFiles == null || imageFiles!!.isEmpty()) {
            Toast.makeText(this, "No images found in Pictures directory.", Toast.LENGTH_LONG).show()
            return
        }
        imageFname = imageFiles!![0].toString()
        myPicture.imagePath = imageFname
    }

    private fun updateImage() {
        imageFname = imageFiles!![curNum].toString()
        myPicture.imagePath = imageFname
        myPicture.invalidate()
    }
}
