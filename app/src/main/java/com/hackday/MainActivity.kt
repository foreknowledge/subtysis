package com.hackday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hackday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        binding.startButton.setOnClickListener {
            showPlayer()
        }
    }

    private fun showPlayer() {
        val playerFragment = PlayerFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(binding.fragmentContainer.id, playerFragment)
            .commit()
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
