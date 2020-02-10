package com.shahim.starrynight.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
             Glide.with(activity!!)
//                 .asBitmap()
            .load(image.url)
            .placeholder(R.drawable.bg_placeholder)
                 .listener(object: RequestListener<Drawable> {
                     override fun onResourceReady(
                         resource: Drawable?,
                         model: Any?,
                         target: Target<Drawable>?,
                         dataSource: DataSource?,
                         isFirstResource: Boolean
                     ): Boolean {
                         startPostponedEnterTransition()
                         return false
                     }

                     override fun onLoadFailed(
                         e: GlideException?,
                         model: Any?,
                         target: Target<Drawable>?,
                         isFirstResource: Boolean
                     ): Boolean {
                         return false
                     }
                 })
                 .into(detail_image)
//            .into(object : CustomTarget<Bitmap>(){
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    startPostponedEnterTransition()
//                    detail_image.setImageBitmap(resource)
//                }
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    // this is called when imageView is cleared on lifecycle call or for
//                    // some other reason.
//                    // if you are referencing the bitmap somewhere else too other than this imageView
//                    // clear it here as you can no longer have the bitmap
//                }
//            })
    }
}
