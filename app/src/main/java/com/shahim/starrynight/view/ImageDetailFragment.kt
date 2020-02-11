package com.shahim.starrynight.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener
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
import java.io.File


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
        if(savedInstanceState == null) {
            init()
        }
        loadData()
    }

    private fun init() {

    }

    /**
     * Load data into view
     */
    private fun loadData() {
        detail_image.transitionName = image.title.replace("\\s".toRegex(), "")
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

        detail_save.setOnClickListener { view ->
            checkSavePermission {
                view.isEnabled = false
                progressBar.visibility = View.VISIBLE
                ImageDownloader.download(requireContext(), image)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = { file ->
                            Snackbar.make(
                                container,
                                R.string.editor_save_success,
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(R.string.editor_save_view_prompt) {

                                    context?.let {
                                        val intent = Intent()
                                        intent.action = Intent.ACTION_VIEW
                                        val photoURI = FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider",file)
                                        intent.setDataAndType(photoURI, "image/jpeg")
//                                    intent.type = "image/jpeg"
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        startActivity(intent)
                                    }
                                }
                                .show()
                            view.isEnabled = true
                            progressBar.visibility = View.GONE
                        },
                        onError = {
                            Snackbar.make(
                                container,
                                R.string.editor_save_error,
                                Snackbar.LENGTH_LONG
                            ).show()
                            view.isEnabled = true
                            progressBar.visibility = View.GONE
                        }
                    )
            }
        }
    }


    /**
     * Check write access permission
     *
     * @param onGrant Callback when permission is granted
     */
    private fun checkSavePermission(onGrant : () -> Unit) {

        val baseLis: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                onGrant()
            }
            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.permission_rat_storage_title)
                        .setMessage(R.string.permission_rat_storage_message_export)
                        .setNegativeButton(R.string.dialog_button_cancel) { dialog, _ ->
                            dialog.dismiss()
                            token.cancelPermissionRequest()
                        }
                        .setPositiveButton(R.string.dialog_button_ok){ dialog, _ ->
                            dialog.dismiss()
                            token.continuePermissionRequest()
                        }
                        .setOnDismissListener { token.cancelPermissionRequest() }
                        .show()
                }
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
            }
        }

        val snackBarLis: PermissionListener = SnackbarOnDeniedPermissionListener.Builder.with(
            activity?.findViewById(R.id.container) as ViewGroup,
            R.string.permission_rat_storage_denied_export)
            .withDuration(5000)
            .build()

        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(CompositePermissionListener(baseLis,snackBarLis)).check()
    }
}
