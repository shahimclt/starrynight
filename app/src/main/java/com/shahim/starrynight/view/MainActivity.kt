package com.shahim.starrynight.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.shahim.starrynight.R
import com.shahim.starrynight.data.DataProvider
import com.shahim.starrynight.model.ImageObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainFragment.GalleryItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(app_bar)
        init()
        loadData()
    }

    private fun init() {

    }

    private var imageList: ArrayList<ImageObject> = ArrayList()

    private lateinit var observer: Disposable

    private fun loadData() {
        observer = DataProvider.getImageObservable(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    imageList = it
                    showImageList()
                },
                onError = {
                    Toast.makeText(this,"Error : "+it.message, Toast.LENGTH_LONG).show()
                }
            )


    }

    private fun showImageList() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container,
                MainFragment.newInstance(imageList)
            )
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        observer.dispose()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if(fragment is MainFragment) {
            fragment.galleryItemClickListener = this
        }
    }

    override fun onGalleryItemClicked(position: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container,
                ImageDetailFragment.newInstance(imageList[position])
            )
            .addToBackStack(null)
            .commit()
    }
}
