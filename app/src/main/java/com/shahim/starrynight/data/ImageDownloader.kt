package com.shahim.starrynight.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.shahim.starrynight.model.ImageObject
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream

object ImageDownloader {
    fun download(c: Context, image: ImageObject): Single<Uri> {
            val imageSaveObservable: Single<Uri> =
                Single.create { emitter ->
                    fun saveImageToGallery(bitmap: Bitmap) {
                        val directory: File =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        val folder = File(directory,"Starry Night")
                        if(!folder.isDirectory) folder.mkdir()
                        val file = File(folder, "SN_" + System.currentTimeMillis() + ".jpg")

                        val outputStream = FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)

                        outputStream.flush()
                        outputStream.fd.sync()
                        outputStream.close()

                        MediaScannerConnection.scanFile(c, arrayOf(file.absolutePath), null, null)

                        emitter.onSuccess(Uri.fromFile(file))
                    }
                    try {
                        val imageUrl = if(image.hdUrl.isNullOrBlank()) image.url else image.hdUrl
                        val uri = Uri.parse(imageUrl)
                        Picasso.with(c)
                            .load(uri)
                            .into(object: Target {
                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                                override fun onBitmapFailed(errorDrawable: Drawable?) {
                                    emitter.onError(UnknownError("Failed loading bitmap"))
                                }
                                override fun onBitmapLoaded(bitmap: Bitmap?,from: Picasso.LoadedFrom?) {
                                    bitmap?.let {
                                        saveImageToGallery(it)
                                    }
                                }
                            })

                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
        return imageSaveObservable
    }
}