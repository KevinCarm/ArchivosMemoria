package com.example.archivosmemoria

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST = 123
    private lateinit var botton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        botton = findViewById(R.id.button)
        botton.setOnClickListener {
            takePicture()
        }
        if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.RECORD_AUDIO
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        applicationContext,
                        android.Manifest.permission.CAMERA
                ) !=
                PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA
            ),
                    1000
            )
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePicture() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(this.packageManager) != null) {
            var imageFile: File? = null
            try {
                imageFile = createImage()
            } catch (e: IOException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
            if (imageFile != null) {
                val uriImage: Uri? = this.let {
                    FileProvider.getUriForFile(
                            it,
                            "com.example.archivosmemoria",
                            imageFile
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage)
            }
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }

    var rute = ""

    @Throws(IOException::class)
    private fun createImage(): File {
        val imageName = "foto_"
        val directory: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", directory)
        rute = image.absolutePath
        Toast.makeText(applicationContext, rute, Toast.LENGTH_SHORT).show()
        return image
    }
}