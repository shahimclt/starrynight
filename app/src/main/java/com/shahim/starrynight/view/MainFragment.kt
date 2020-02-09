package com.shahim.starrynight.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahim.starrynight.R
import com.shahim.starrynight.model.ImageObject
import com.shahim.starrynight.view.adapter.ImageListQuickAdapter
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance(_imageList: ArrayList<ImageObject>): MainFragment {
            val frag = MainFragment()
            frag.imageList = _imageList
            return frag
        }
    }

    private lateinit var imageList: ArrayList<ImageObject>

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
        init()
    }

    private fun init() {
        initAdapter()
    }


    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        mAdapter = ImageListQuickAdapter(imageList)
        recyclerView.adapter = mAdapter

        if(imageList.isNullOrEmpty()) mAdapter.setEmptyView(R.layout.list_empty_view)

        mAdapter.setOnItemClickListener { adapter, view, position ->

        }
    }
}
