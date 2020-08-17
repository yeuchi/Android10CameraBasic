package com.ctyeung.scopedstorage

import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class PhotoStorage(val context:Context) {
    var displayname:String?=null
    var bucketName:String?=null
    var imageUri:Uri?=null
    var values:ContentValues?=null

    fun setNames(displayname:String, bucketName:String) {
        this.displayname = displayname
        this.bucketName = bucketName

        values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayname)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$bucketName/")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        imageUri = context.contentResolver.insert(collection, values)
    }

    fun read(contentResolver: ContentResolver,imageView:ImageView):Bitmap? {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            return bitmap
        }
        catch (e:Exception){
            return null
        }
    }

    fun read(photoPath:String, imageView:ImageView):Bitmap? {
        // Get the dimensions of the View
        val targetW = imageView.width
        val targetH = imageView.height

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(photoPath, bmOptions)
        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true
        val bitmap = BitmapFactory.decodeFile(photoPath, bmOptions)
        return bitmap
    }

    fun save(bmp:Bitmap):String {
        if(imageUri != null && values != null) {
            try {
                context.contentResolver.openOutputStream(imageUri!!).use { out ->
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
            } catch (e: Exception) {
                Log.d("PhtoStorage", "openOutputStream failed");
                return e.toString()
            }

            values?.clear()
            values?.put(MediaStore.Images.Media.IS_PENDING, 0)
            context.contentResolver.update(imageUri!!, values, null, null)
            return ""
        }
        return "missing imageUri or values"
    }

    fun delete(context:Context):Boolean {
        if(imageUri != null) {
            DocumentsContract.deleteDocument(context.contentResolver, imageUri!!)
            return true
        }
        return false
    }
}