package com.shahim.starrynight.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.shahim.starrynight.model.ImageObject
import com.squareup.picasso.Picasso
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream

/**
 * Utility class to save an image to gallery
 */
object ImageDownloader {

    /**
     * Saves the image to gallery. This methods tries to access the ImageObject.hdurl first. If that is not set, it falls back to ImageObject.url
     * The image is saved to public gallery
     *
     * @param c context
     * @param image ImageObject instance
     * @return RxKotlin observable which returns a reference to the saved file on success.
     */
    fun download(c: Context, image: ImageObject): Single<File> {
        return Single.create { emitter ->
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

                emitter.onSuccess(file)
            }
            try {
                val imageUrl = if(image.hdUrl.isNullOrBlank()) image.url else image.hdUrl
                val uri = Uri.parse(imageUrl)
                saveImageToGallery(Picasso.with(c).load(uri).get())
//                    .into(object: Target {
//                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
//                        override fun onBitmapFailed(errorDrawable: Drawable?) {
//                            emitter.onError(UnknownError("Failed loading bitmap"))
//                        }
//                        override fun onBitmapLoaded(bitmap: Bitmap?,from: Picasso.LoadedFrom?) {
//                            bitmap?.let {
//                                it)
//                            }
//                        }
//                    })

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }
}