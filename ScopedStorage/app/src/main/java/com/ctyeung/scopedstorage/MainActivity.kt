package com.ctyeung.scopedstorage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
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

    val DELETE_PERMISSION_REQUEST = 0x1033
    val REQUEST_TAKE_PHOTO = 1
    var photoURI:Uri?= null
    var currentPhotoPath: String?=null
    lateinit var photoStore:PhotoStorage
    lateinit var context: Context
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.layout = this
        context = this
        photoStore = PhotoStorage(this)
    }

    fun onClickBtnMemory() {
        txtSelected.text = "memory"

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Don't call resolve in Android 11, API 30
        // if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
       // }
    }

    fun onClickBtnPhotoUri() {
        txtSelected.text = "uri"
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Don't call resolve in Android 11, API 30
        // if (takePictureIntent.resolveActivity(packageManager) != null) {

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

    /*
     * Read
     */
    fun onClickBtnLoad() {
        var bitmap:Bitmap?=null
        if(currentPhotoPath!=null) {
            bitmap = photoStore.read(currentPhotoPath!!, imageView)
        }
        else {
            bitmap = photoStore.read(this.contentResolver, imageView)
        }

        if(bitmap != null) {
            binding?.layout?.imageView?.setImageBitmap(bitmap)
            return
        }
        Toast.makeText(this, "read failed", Toast.LENGTH_LONG).show()
    }

    /*
     * 1st time or over-write
     */
    fun onClickBtnSave() {
        val w = binding?.imageView?.width
        val h = binding?.imageView?.height
        val drawable = binding?.layout?.imageView?.drawable
        val bitmap = drawable?.toBitmap(w, h)
        if(bitmap != null) {
            val retVal = photoStore.save(bitmap)
            if (retVal.length==0)
                return
        }
        Toast.makeText(this, "Save failed", Toast.LENGTH_LONG).show()
    }

    /*
     * Android 11 requirement 
     * -> move to trash, not delete
     */
    fun onClickBtnTrash() {
        val uris = listOf(photoStore.imageUri)
        val pendingIntent = MediaStore.createDeleteRequest(contentResolver, uris.filter {
            checkUriPermission(it, Binder.getCallingPid(), Binder.getCallingUid(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION) != PackageManager.PERMISSION_GRANTED
        })
        startIntentSenderForResult(pendingIntent.intentSender, DELETE_PERMISSION_REQUEST, null, 0, 0, 0)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == RESULT_OK) {
                    handleTakePhoto(data)
                }
            }

            DELETE_PERMISSION_REQUEST -> {
                photoStore.trash(this)
            }

            else -> {
                Toast.makeText(this, "bad request code", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun handleTakePhoto(data: Intent?) {
        var imageBitmap:Bitmap?=null

        if (data != null && data?.extras != null) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            binding?.layout?.imageView?.setImageBitmap(imageBitmap!!)
            photoStore.setNames("hello", "goldBucket")
            val returned = photoStore.save(imageBitmap)

            if(returned != "")
                Toast.makeText(this, returned, Toast.LENGTH_LONG).show()
        }
        else if(currentPhotoPath!=null) {

            val bitmap = photoStore.read(currentPhotoPath!!, imageView)
            binding?.layout?.imageView?.setImageBitmap(bitmap)
            galleryAddPic(currentPhotoPath!!)
        }
        //revokeUriPermission(photoURI, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
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