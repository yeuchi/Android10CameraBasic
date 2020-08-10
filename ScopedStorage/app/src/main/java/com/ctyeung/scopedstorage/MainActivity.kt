package com.ctyeung.scopedstorage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.ctyeung.scopedstorage.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*
 * https://developer.android.com/training/camera/photobasics#java
 */

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val REQUEST_TAKE_PHOTO = 1
    var photoURI:Uri?= null
    var currentPhotoPath: String?=null
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.layout = this
        context = this
    }

    fun onClickBtnMemory() {
        txtSelected.text = "memory"

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    fun onClickBtnPhotoUri() {
        txtSelected.text = "uri"

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, "photoFile Error", Toast.LENGTH_LONG).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                            context,
                            "com.ctyeung.scopedstorage.provider",
                            it
                    )
                    grantUriPermission("com.ctyeung.scopedstorage.provider", photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            var imageBitmap:Bitmap?=null
            var photoStore = PhotoStorage(this)


            if (data != null) {
                imageBitmap = data?.extras?.get("data") as Bitmap
                binding?.layout?.imageView?.setImageBitmap(imageBitmap!!)
                photoStore.setNames("hello", "goldBucket")
                val returned = photoStore.save(imageBitmap)

                if(returned != "")
                    Toast.makeText(this, returned, Toast.LENGTH_LONG).show()
            }
            else if(currentPhotoPath!=null) {

                val path = photoURI?.path
                val bitmap = photoStore.read(currentPhotoPath!!, imageView)
                binding?.layout?.imageView?.setImageBitmap(bitmap)
                galleryAddPic(currentPhotoPath!!)
            }
            revokeUriPermission(photoURI, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
    }

    /*
     * Scoped Storage article
     * https://proandroiddev.com/working-with-scoped-storage-8a7e7cafea3
     */

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun galleryAddPic(photoPath:String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(photoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }
}