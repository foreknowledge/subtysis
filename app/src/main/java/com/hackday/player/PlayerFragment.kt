package com.hackday.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.hackday.R
import com.hackday.databinding.FragmentPlayerBinding
import com.hackday.databinding.ItemShoppingBinding
import com.hackday.player.adapter.MetadataRecyclerViewAdapter
import com.hackday.subtysis.ResponseListener
import com.hackday.subtysis.SubtitleParserImpl
import com.hackday.subtysis.Subtysis
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.Subtitle
import com.hackday.utils.Toaster
import kotlinx.android.synthetic.main.fragment_player.*
import java.io.File

/**
 * @author Created by lee.cm on 2019-11-12.
 */
class PlayerFragment : Fragment() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var subtitleFilePath: String
    private lateinit var sonthread: NewThread

    private val shoppingAdapter = MetadataRecyclerViewAdapter<ItemShoppingBinding, Keyword>(
        R.layout.item_shopping,
        BR.item
    )

    private var player: SimpleExoPlayer? = null

    private val mHandler: Handler = Handler { msg ->
        val metadata = viewModel.displayData.value
        val subtitles = getSubtitles()

        if (player != null) {
            if (player != null) {
                if (msg.what == 0) {
                    for (i in subtitles.indices) {
                        if (subtitles[i].frame > player!!.currentPosition && i > 0) {

                            if (subtitles[i - 1].frame > (player!!.currentPosition - 1000)) {
                                if (subtitleview != null) {
                                    subtitleview.text = subtitles[i - 1].sentence

                                    if (metadata != null) {
                                        val filteredData = metadata.filter {
                                            subtitles[i - 1].sentence.contains(it.word)
                                                    && it.metadata != null
                                        }

                                        if (filteredData.isNotEmpty()) {
                                            viewModel.setDisplayData(filteredData)
                                            viewModel.setSheetVisibility(true)
                                        } else {
                                            viewModel.setSheetVisibility(false)
                                        }
                                    }
                                }
                            } else {
                                subtitleview.text = ""
                            }
                            break
                        }
                    }
                }
            }
        }

        true
    }

    private fun getSubtitles(): List<Subtitle> {
        val c = SubtitleParserImpl()
        return c.createSubtitle(subtitleFilePath)
    }

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
        sonthread = NewThread(mHandler)
        sonthread.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player, container, false
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            val subTitleFilePath = arguments.getString("subTitleFilePath")
            this.subtitleFilePath = subTitleFilePath!!
        }
        getSubtitles()
        init()

        subscribeViewModel()
    }

    private fun init() {
        if (viewModel.hasVideoSourceUri()) {
            return
        }
        showlist.setOnClickListener {
            if (infotext.visibility == View.VISIBLE) {
                infotext.visibility = View.INVISIBLE
            } else {
                infotext.visibility = View.VISIBLE
            }
            check.text = R.id.showlist.toString()
        }
        selectMediaUri()
        startSubtitleAnalyze()
        binding.rvMetadata.adapter = shoppingAdapter
    }

    private fun startSubtitleAnalyze() {
        Subtysis(
            File(this.subtitleFilePath),
            listOf(SearchType.SHOPPING)
        ).analyze(object : ResponseListener {
            override fun onResponse(keywords: List<Keyword>?) {
                keywords?.let {
                    viewModel.setDisplayData(keywords)
                    Log.d("Test", "keywords = $keywords")
                }
            }

            override fun onFailure(errorMsg: String?) {
                Toaster.showShort(errorMsg ?: "Subtitle analyze error")
            }

        })
    }

    private fun subscribeViewModel() {
        viewModel.getVideoSourceUriLiveData().observe(viewLifecycleOwner, Observer {
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

class NewThread(private val data: Handler) : Thread() {

    override fun run() {

        while (true) {
            try {
                sleep(1000)
                data.sendEmptyMessage(0)
            } catch (e: InterruptedException) {
                break
            }

        }
    }

}

