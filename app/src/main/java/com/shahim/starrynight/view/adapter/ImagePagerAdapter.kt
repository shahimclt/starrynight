package com.shahim.starrynight.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.ImageDetailFragment


class ImagePagerAdapter(fa: Fragment, private val images: ArrayList<ImageObject>) :
    FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        val image = images[position]
        return ImageDetailFragment.newInstance(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

}