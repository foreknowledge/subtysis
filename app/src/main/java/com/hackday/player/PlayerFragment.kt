package com.hackday.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.hackday.databinding.FragmentPlayerBinding
import com.hackday.subtysis.SetResponseListener
import com.hackday.subtysis.Subtysis
import com.hackday.subtysis.model.SearchType
import com.hackday.utils.Toaster
import java.io.File


/**
 * @author Created by lee.cm on 2019-11-12.
 */
class PlayerFragment : Fragment() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentPlayerBinding

    private var player: SimpleExoPlayer? = null

    companion object {
        @JvmStatic
        fun newInstance(): PlayerFragment {
            val args = Bundle()
            val fragment = PlayerFragment()
            fragment.arguments = args
            return fragment
        }

        const val REQUEST_CODE_FOR_OPEN_DOCUMENT = 1101
        const val MIME_TYPE_VIDEO_ALL = MimeTypes.BASE_TYPE_VIDEO + "/*"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[PlayerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.hackday.R.layout.fragment_player, container, false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        subscribeViewModel()
    }

    private fun init() {
        if (viewModel.hasVideoSourceUri()) {
            return
        }

        selectMediaUri()
        startSubtitleAnalyze()
    }

    private fun startSubtitleAnalyze() {
        val subtysis = Subtysis()
        subtysis.init(File(""), arrayListOf(SearchType.SHOPPING))
        subtysis.setOnResponseListener(SetResponseListener { keywords ->
            Log.d("keywords", keywords.joinToString(""))
            viewModel.setDisplayData(keywords)
        })
        subtysis.analyze()
    }

    private fun subscribeViewModel() {
        viewModel.getVideoSourceUriLiveData().observe(this, Observer {
            if (it == null) {
                Toaster.showShort("cannot start player with null uri")
            } else {
                initPlayer(it)
            }
        })
    }

    private fun selectMediaUri() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI).also {
            it.type = MIME_TYPE_VIDEO_ALL
        }

        val packageManager = activity?.packageManager
        packageManager?.let {
            if (intent.resolveActivity(it) != null) {
                startActivityForResult(intent, REQUEST_CODE_FOR_OPEN_DOCUMENT)
            } else {
                Toaster.showShort("cannot handle this intent...")
            }
        }
    }

    private fun initPlayer(mediaUri: Uri) {
        player = ExoPlayerFactory.newSimpleInstance(context)
        binding.playerView.player = player

        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context?.packageName)
        )

        val videoSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri)

        player!!.prepare(videoSource)
    }

    override fun onDestroyView() {
        player?.release()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultIntent)
        if (requestCode == REQUEST_CODE_FOR_OPEN_DOCUMENT && resultIntent != null) {
            viewModel.setVideoSourceUri(resultIntent.data)
        }
    }
}