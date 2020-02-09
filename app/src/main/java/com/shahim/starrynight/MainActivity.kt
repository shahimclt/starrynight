package com.shahim.starrynight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.shahim.starrynight.data.DataProvider
import com.shahim.starrynight.model.ImageObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private lateinit var imageList: List<ImageObject>

    private fun init() {
        val observer = DataProvider.getImageObservable(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    imageList = it
                    Toast.makeText(this,"Loaded : "+imageList.size,Toast.LENGTH_LONG).show()
                },
                onError = {
                    Toast.makeText(this,"Error : "+it.message,Toast.LENGTH_LONG).show()
                }
            )
    }
}
