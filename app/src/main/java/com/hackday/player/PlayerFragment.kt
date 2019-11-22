package com.hackday.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.hackday.subtysis.SetResponseListener
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

    private lateinit var arr: ArrayList<Subtitle>

    private val shoppingAdapter = MetadataRecyclerViewAdapter<ItemShoppingBinding, Keyword>(
        R.layout.item_shopping,
        BR.item
    )

    private var player: SimpleExoPlayer? = null

    fun getarray() {
        var c = SubtitleParserImpl()
        arr = c.createSubtitle(subtitleFilePath)
    }

    val mHandler: Handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            if (player != null && arr != null) {
                if (msg.what == 0) {
                    for (i in arr.indices) {
                        if (arr[i].frame > player!!.currentPosition && i > 0) {
                            var index: Int = i

                            if (arr[index - 1].frame > (player!!.currentPosition - 1000)) {
                                subtitleview.setText((arr[index - 1].sentence))

                                var str = arr[index - 1].sentence.split(" ")
                                infotext.removeAllViews()
                                for (i in str.indices) {
                                    var bat = Button(this@PlayerFragment.context)
                                    bat.setText(str[i])
                                    infotext.addView(bat)
                                }
                            } else {
                                subtitleview.setText("");


                            }
                            break
                        }
                    }

                }
            }
        }

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
        var c = NewThread(mHandler)
        c.start()
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
        getarray()
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
            check.setText(R.id.showlist.toString())
        }
        selectMediaUri()
        startSubtitleAnalyze()
        binding.rvMetadata.adapter = shoppingAdapter
    }

    private fun startSubtitleAnalyze() {
        Subtysis(File(this.subtitleFilePath), arrayListOf(SearchType.SHOPPING)).analyze(object :
            SetResponseListener {
            override fun onResponse(keywords: java.util.ArrayList<Keyword>?) {
                keywords?.let {
                    viewModel.setDisplayData(keywords)
                }
            }

            override fun onFailure(errorMsg: String?) {
                Toaster.showShort(errorMsg ?: "Subtitle analyze error")
            }

        })
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

class NewThread(var data:Handler) : Thread( ) {

    override fun run() {

        while (true) {
            sleep(1000)
            data.sendEmptyMessage(0)
        }
    }
}

