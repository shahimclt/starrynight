package com.shahim.starrynight.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.shahim.starrynight.R
import com.shahim.starrynight.data.DataProvider
import com.shahim.starrynight.model.ImageObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

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
        observer = DataProvider.fetchImages(this)
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

    /**
     * Interface method to notify activity on item click on the image grid
     *
     * @param position position of the selected image
     * @param imageView view that was clicked
     */
    override fun onGalleryItemClicked(position: Int, imageView: ImageView) {
        val transitionName = ViewCompat.getTransitionName(imageView) ?:""
        val intent = ImagePagerActivity.craftIntent(this,position)
        val options = ActivityOptions.makeSceneTransitionAnimation(this,imageView,transitionName)
        startActivity(intent,options.toBundle())
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        val position: Int = ImagePagerActivity.getPosition(resultCode, data);
        if (position != RecyclerView.NO_POSITION) {
            recyclerView.scrollToPosition(position)
        }
    }
}
