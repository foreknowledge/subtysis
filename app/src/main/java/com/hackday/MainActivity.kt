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
import com.hackday.subtysis.ResponseListener
import com.hackday.subtysis.Subtysis
import com.hackday.subtysis.model.Keyword
import com.hackday.subtysis.model.SearchType
import com.hackday.subtysis.model.items.ShoppingItem
import com.hackday.utils.Toaster
import java.io.File
import java.io.FileOutputStream


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
//        checkPermission()
//        init()
//        loadAsset()

        showLog()
    }

    private fun showLog() {
        Subtysis(
            File(""),
            arrayListOf(SearchType.SHOPPING)
        ).analyze(object : ResponseListener {
            override fun onResponse(keywords: ArrayList<Keyword>?) {
                keywords?.let {
                    for ((_, word, _, metadata) in keywords) {
                        Log.d("Test", "keyword = $word")
                        val data = metadata?.get(SearchType.SHOPPING)
                        for (baseItem in data?.items!!) {
                            val item = baseItem as ShoppingItem
                            Log.d(
                                "Test",
                                "title = [" + item.title + "], mallName = [" + item.mallName + "]"
                            )
                        }
                    }
                }
            }

            override fun onFailure(errorMsg: String?) {
                Toaster.showShort(errorMsg ?: "Subtitle analyze error")
            }

        })
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
