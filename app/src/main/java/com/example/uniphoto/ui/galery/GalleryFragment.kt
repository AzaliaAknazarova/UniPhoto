package com.example.uniphoto.ui.galery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.kodein.KodeinFragment
import com.example.uniphoto.model.MaskItemsListAdapter
import com.example.uniphoto.model.VideoItemsListAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.fragment_main.*

class GalleryFragment: KodeinFragment<GalleryViewModel>() {

    override val viewModel by viewModel(GalleryViewModel::class.java)

    val videoItemsAdapter = VideoItemsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViewModel()
        viewModel.init(requireContext())
    }

    fun initViews() {
        galleryItemsRecyclerView.adapter = videoItemsAdapter
        backpressedImageView.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun bindViewModel() {

        bind(viewModel.videoItemsList) {
            videoItemsAdapter.items = it
            videoItemsAdapter.notifyDataSetChanged()
        }

        bindCommand(viewModel.launchVideoViewCommand) {
            val arg = Bundle().apply { putString(galleryFragmentArg, it) }
            navigate(R.id.action_galleryFragment_to_photoViewFragment, arg)
        }
    }

    companion object {
        val galleryFragmentArg: String = ::galleryFragmentArg.name
    }
}