package com.hackday

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hackday.databinding.ActivityMainBinding
import com.hackday.player.PlayerFragment
import com.hackday.subtysis.RequestManager

class MainActivity : AppCompatActivity() {

    companion object {
        private val VIDEO_DOWNLOAD_LINK =
            "https://drive.google.com/open?id=1ix5k-wI9k2flHgpgEf8Z-NYmtB5rRW-c"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        RequestManager.createRequestQueue(this@MainActivity)

        binding.startButton.setOnClickListener {
            showPlayer()
        }

        binding.downloadVideoButton.setOnClickListener {
            downloadVideo()
        }
    }

    private fun showPlayer() {
        val playerFragment = PlayerFragment.newInstance()
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

}
