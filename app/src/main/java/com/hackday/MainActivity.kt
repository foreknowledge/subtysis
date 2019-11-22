package com.hackday

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.hackday.databinding.ActivityMainBinding
import com.hackday.player.PlayerFragment
import com.hackday.subtysis.RequestManager
import com.hackday.subtysis.SetResponseListener
import com.hackday.subtysis.Subtysis
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.items.BlogItem
import com.hackday.subtysis.model.items.EncyclopediaItem
import com.hackday.subtysis.model.items.ShoppingItem
import com.hackday.utils.Toaster
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val VIDEO_DOWNLOAD_LINK =
            "https://drive.google.com/open?id=1ix5k-wI9k2flHgpgEf8Z-NYmtB5rRW-c"
        private const val ASSET_FILE_NAME =
            "치아문난난적소시광(우리의 따뜻했던 시절에게)_23회_NonDRM_[최소화질]_253891057.smi"
        private const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val REQUEST_CODE_FOR_PERMISSION = 300
    }

    private lateinit var binding: ActivityMainBinding
    private var subtitleFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        checkPermission()
        init()
        loadAsset()

        showLog()
    }

    private fun showLog() {
        val subtysis = Subtysis()

        val types = ArrayList<SearchType>()
        types.add(SearchType.SHOPPING)
//        types.add(SearchType.ENCYCLOPEDIA)
//        types.add(SearchType.BLOG)

        subtysis.init(subtitleFile!!, types)
        subtysis.setOnResponseListener(object : SetResponseListener {
            override fun onResponse(keywords: ArrayList<Keyword>) {
                setMyKeywords(keywords)
            }

            override fun onFailure(errorMsg: String) {
                Log.d("Log", errorMsg)
            }
        }).analyze()
    }

    private fun setMyKeywords(keywords: ArrayList<Keyword>) {
        for (keyword in keywords) {
            Log.d("Log", "keyword ====== ${keyword.word}")
            val results = keyword.responses

            Log.d("Log", "===========SHOPPING DATA===========")

            if (results != null) {
                val shoppingData = results[SearchType.SHOPPING]
                for (baseItem in shoppingData!!.items) {
                    val shoppingItem = baseItem as ShoppingItem
                    Log.d(
                        "Log",
                        "title = [" + shoppingItem.title + "], mallName = [" + shoppingItem.mallName + "]"
                    )
                }
            }

//            Log.d("Log", "===========ENCYCLOPEDIA DATA===========")
//            val encyclopData = results[SearchType.ENCYCLOPEDIA]
//            for (baseItem in encyclopData!!.items) {
//                val encyclopediaItem = baseItem as EncyclopediaItem
//                Log.d(
//                    "Log",
//                    "title = [" + encyclopediaItem.title + "], description = [" + encyclopediaItem.description + "]"
//                )
//            }
//
//            Log.d("Log", "===========BLOG DATA===========")
//            val blogData = results[SearchType.BLOG]
//            for (baseItem in blogData!!.items) {
//                val blogItem = baseItem as BlogItem
//                Log.d(
//                    "Log",
//                    "title = [" + blogItem.title + "], description = [" + blogItem.bloggername + "]"
//                )
//            }
        }
    }

    private fun init() {
        binding.startButton.setOnClickListener {
            showPlayer()
        }

        binding.downloadVideoButton.setOnClickListener {
            downloadVideo()
        }

        binding.checkAsset.setOnClickListener {
            checkAsset()
        }
    }

    private fun checkAsset() {
        subtitleFile?.let {
            Toaster.showShort("path : " + it.absolutePath + " , isExist ? " + it.exists() + " , size : " + it.length())
        }
    }

    private fun loadAsset() {
        val file = File(filesDir.absolutePath + "/" + ASSET_FILE_NAME)
        if (file.exists()) {
            subtitleFile = file
            return
        }
        val inputStream = resources.assets.open(ASSET_FILE_NAME)
        val outputStream = FileOutputStream(file)

        AsyncTask.THREAD_POOL_EXECUTOR.execute {
            var buffer: Int
            do {
                buffer = inputStream.read()
                outputStream.write(buffer)
            } while (buffer > 0)

            subtitleFile = file
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                STORAGE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(STORAGE_PERMISSION),
            REQUEST_CODE_FOR_PERMISSION
        )
    }

    private fun showPlayer() {
        val playerFragment = PlayerFragment.newInstance()

        val arguments = Bundle()
        arguments.putString("subTitleFilePath", subtitleFile!!.path)

        playerFragment.arguments = arguments

        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainer.id, playerFragment)
            .commit()
    }

    private fun downloadVideo() {
        Intent(Intent.ACTION_VIEW, Uri.parse(VIDEO_DOWNLOAD_LINK)).also { startActivity(it) }
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(binding.fragmentContainer.id)?.let {
            supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()

            return
        }

        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FOR_PERMISSION) {
            if (resultCode == Activity.RESULT_OK) {
                Toaster.showShort("권한 획득!")
            } else {
                Toaster.showShort("권한이 없으면 앱을 강제 종료합니다.")
                finish()
            }
        }
    }
}
