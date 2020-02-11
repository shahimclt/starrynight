package com.shahim.starrynight.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.shahim.starrynight.R
import com.shahim.starrynight.data.ImageDownloader
import com.shahim.starrynight.model.ImageObject
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_image_detail.*
import org.threeten.bp.format.DateTimeFormatter


class ImageDetailFragment : Fragment() {

    companion object {
        fun newInstance(image: ImageObject): ImageDetailFragment {
            val frag = ImageDetailFragment()
            frag.image = image
            return frag
        }
    }

    lateinit var fragmentLoadedListener: FragmentLoadedListener

    interface FragmentLoadedListener {
        fun onFragmentLoaded()
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
                    fragmentLoadedListener?.onFragmentLoaded()
                }

                override fun onError() {
                    startPostponedEnterTransition()
                    fragmentLoadedListener?.onFragmentLoaded() 
                }
            })

        detail_title.text = image.title
        detail_copyright.text = image.copyright
        detail_desc.text = image.explanation
        detail_date.text = image.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

        detail_save.setOnClickListener {view ->
           view.isEnabled = false
            progressBar.visibility = View.VISIBLE
            ImageDownloader.download(requireContext(),image)
                .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {uri ->
                    Snackbar.make(container, R.string.editor_save_success, Snackbar.LENGTH_LONG)
                        .setAction(R.string.editor_save_view_prompt) {
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.setDataAndType(uri,"image/jpeg")
                            intent.type = "image/jpeg"
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                    view.isEnabled = true
                    progressBar.visibility = View.GONE
                },
                onError = {
                    Snackbar.make(container, R.string.editor_save_success, Snackbar.LENGTH_LONG).show()
                    view.isEnabled = true
                    progressBar.visibility = View.GONE
                }
            )
        }
    }
}
