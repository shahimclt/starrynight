package com.shahim.starrynight.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.shahim.starrynight.R
import com.shahim.starrynight.data.DataProvider
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.adapter.ImagePagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_pager.*


class ImagePagerActivity : AppCompatActivity(), ImageDetailFragment.FragmentLoadedListener {

    companion object {
        private const val KEY_POSITION = "currentPos"
        fun craftIntent(c: Context,_initialPos: Int): Intent {
            val intent = Intent(c,ImagePagerActivity::class.java)
            intent.putExtra(KEY_POSITION,_initialPos)
            return intent
        }

        fun getPosition(resultCode: Int, data: Intent?): Int {
            if(resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    return data.getIntExtra(KEY_POSITION,RecyclerView.NO_POSITION)
                }
            }
            return RecyclerView.NO_POSITION
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pager)
        ActivityCompat.postponeEnterTransition(this)
//        sharedElementEnterTransition = TransitionInflater.from(this)
//            .inflateTransition(android.R.transition.move)
        setSupportActionBar(app_bar)
        init()
        loadData()
    }

    private var imageList: ArrayList<ImageObject> = ArrayList()

    private var initialPosition: Int = 0

    private lateinit var observer: Disposable

    private fun init() {
        initialPosition = intent.getIntExtra(KEY_POSITION,0)
    }

    private fun loadData() {
        observer = DataProvider.getImageObservable(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    imageList = it
                    showImage()
                },
                onError = {
                    Toast.makeText(this,"Error : "+it.message, Toast.LENGTH_LONG).show()
                }
            )
    }

    /**
     * Initalizes the Viewpager to show an image at the selected index
     *
     */
    private fun showImage() {
        val imagePagerAdapter = ImagePagerAdapter(this,imageList)
        view_pager.adapter = imagePagerAdapter
        view_pager.setCurrentItem(initialPosition,false)
    }

    override fun onDestroy() {
        super.onDestroy()
        observer.dispose()
    }

    // Methods for shared element transitions

    override fun onAttachFragment(fragment: Fragment) {
        if(fragment is ImageDetailFragment) {
            fragment.fragmentLoadedListener = this
        }
    }

    override fun onFragmentLoaded() {
        ActivityCompat.startPostponedEnterTransition(this)
    }

    override fun finishAfterTransition() {
        setResult()
        super.finishAfterTransition()
    }

    private fun setResult() {
        val position: Int = view_pager.currentItem
        val data = Intent()
        data.putExtra(KEY_POSITION, position)
        setResult(Activity.RESULT_OK, data)
    }
}
