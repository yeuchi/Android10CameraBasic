package com.ctyeung.scopedstorage

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.lang.Exception

class PhotoStorage(val context:Context) {
    var displayname:String?=null
    var bucketName:String?=null
    var imageUri:Uri?=null
    var values:ContentValues?=null

    fun setNames(displayname:String, bucketName:String){
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
}