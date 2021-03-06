package com.shahim.starrynight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.adapter.ImageListQuickAdapter
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    lateinit var galleryItemClickListener: GalleryItemClickListener

    interface GalleryItemClickListener {
        /**
         * Interface method to notify activity on item click on the image grid
         *
         * @param position position of the selected image
         * @param imageView view that was clicked
         */
        fun onGalleryItemClicked( position: Int, imageView: ImageView)
    }

    companion object {
        fun newInstance(_imageList: ArrayList<ImageObject>): MainFragment {
            val frag = MainFragment()
            frag.imageList = _imageList
            return frag
        }
    }

    private var imageList: ArrayList<ImageObject> = ArrayList()

    private lateinit var mAdapter: ImageListQuickAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null) {
            init()
        }
    }

    private fun init() {
        initAdapter()
    }


    private fun initAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = null
        recyclerView.setHasFixedSize(true)
        mAdapter = ImageListQuickAdapter(imageList)
        recyclerView.adapter = mAdapter

        if(imageList.isNullOrEmpty()) mAdapter.setEmptyView(R.layout.list_empty_view)

        mAdapter.setOnItemClickListener { _, view, position ->
            galleryItemClickListener?.onGalleryItemClicked(position,view.findViewById(R.id.list_image))
        }
    }
}
