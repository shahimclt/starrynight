package com.shahim.starrynight.data

import android.content.Context
import android.media.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Provides Utility methods to fetch data
 */
object DataProvider {

    private var cachedImagesObservable: Single<ArrayList<ImageObject>>? = null

    /**
     * Async fetch the data from the source. Subscribe to the observable to get the actual results
     *
     * @param c context
     * @return RxJava Single observable which returns ArrayList<ImageObject>
     */
    fun fetchImages(c: Context): Single<ArrayList<ImageObject>> {
        if(cachedImagesObservable == null) {
            val imagesObservable: Single<ArrayList<ImageObject>> =
                Single.create { emitter ->

                    /**
                     * Read the contents of an InputStream into a string
                     *
                     * @param inputStream Inputstream to read
                     * @return String with the read data
                     */
                    fun readTextFile(inputStream: InputStream): String {
                        val outputStream = ByteArrayOutputStream()
                        val buf = ByteArray(1024)
                        var len: Int
                        try {
                            while (inputStream.read(buf).also { len = it } != -1) {
                                outputStream.write(buf, 0, len)
                            }
                            outputStream.close()
                            inputStream.close()
                        } catch (e: IOException) {
                            emitter.onError(e)
                        }
                        return outputStream.toString()
                    }
                    try {
                        val xmlFileInputStream: InputStream = c.resources.openRawResource(R.raw.data)

                        val jsonString: String = readTextFile(xmlFileInputStream)

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<ImageObject>>() {}.type
                        val images: ArrayList<ImageObject> = gson.fromJson(jsonString,type)

                        /* Sort by date: Descending */
                        images.sortWith(Comparator{a: ImageObject,b: ImageObject -> b.date.compareTo(a.date)})
                        emitter.onSuccess(images)

                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
            cachedImagesObservable = imagesObservable.cache()
        }
        return cachedImagesObservable!!
    }
}