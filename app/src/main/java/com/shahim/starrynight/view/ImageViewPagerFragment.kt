package com.shahim.starrynight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.adapter.ImagePagerAdapter
import kotlinx.android.synthetic.main.fragment_image_pager.*


class ImageViewPagerFragment : Fragment() {

    companion object {
        fun newInstance(_images: ArrayList<ImageObject>, _initialPos: Int): ImageViewPagerFragment {
            val frag = ImageViewPagerFragment()
            frag.images = _images
            frag.initialPosition = _initialPos
            return frag
        }
    }

    private lateinit var images: ArrayList<ImageObject>

    private var initialPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val imagePagerAdapter = ImagePagerAdapter(this,images)
        view_pager.adapter = imagePagerAdapter
        view_pager.setCurrentItem(initialPosition,false)
    }
}
