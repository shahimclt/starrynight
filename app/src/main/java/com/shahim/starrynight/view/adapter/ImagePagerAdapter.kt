package com.shahim.starrynight.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.ImageDetailFragment


class ImagePagerAdapter(fm: FragmentManager, private val images: ArrayList<ImageObject>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val image = images[position]
        return ImageDetailFragment.newInstance(image)
    }

    override fun getCount(): Int {
        return images.size
    }

}