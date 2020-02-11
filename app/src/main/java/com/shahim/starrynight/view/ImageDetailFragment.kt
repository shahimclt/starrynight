package com.shahim.starrynight.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image_detail.*


class ImageDetailFragment : Fragment() {

    companion object {
        fun newInstance(image: ImageObject): ImageDetailFragment {
            val frag = ImageDetailFragment()
            frag.image = image
            return frag
        }
    }

    private lateinit var image: ImageObject

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
        return inflater.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail_image.transitionName = image.title.replace("\\s".toRegex(), "")
        init()
    }

    private fun init() {
        Picasso.with(context)
            .load(image.url)
            .placeholder(R.drawable.bg_placeholder)
            .into(detail_image, object : Callback {
                override fun onSuccess() {
                    startPostponedEnterTransition()
                }

                override fun onError() {
                    startPostponedEnterTransition()
                }
            })
    }
}
