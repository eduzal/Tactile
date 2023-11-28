package com.ligalight.tactile

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ligalight.tactile.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketListener: WebSocketListener
    private lateinit var viewModel: MainViewModel

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val DEFAULT_ANIMATION_FLAG = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = WebSocketListener(viewModel)

        viewModel.animationFlags.observe(this, Observer { flags ->
            updateFragmentAnimation(flags)
        })

        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)

        setSupportActionBar(binding.bottomAppBar)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomHome -> {
                    replaceFragment(HomeFragment(), "HomeFragment")
                    true
                }
                R.id.bottomBass -> {
                    replaceFragment(BassFragment(),"BassFragment")
                    val flags = booleanArrayOf(true, false, false) // Example boolean array
                    viewModel.setAnimationFlags(flags)
                    true
                }
                R.id.bottomMedium -> {
                    replaceFragment(MediumFragment(),"MediumFragment")
                    val flags = booleanArrayOf(false, true, false) // Example boolean array
                    viewModel.setAnimationFlags(flags)
                    true
                }
                R.id.bottomHigh -> {
                    replaceFragment(TrebleFragment(), "TrebleFragment")
                    val flags = booleanArrayOf(false, false, true) // Example boolean array
                    viewModel.setAnimationFlags(flags)
                    true
                }
                else -> false

            }
        }
        fragmentManager = supportFragmentManager
        replaceFragment(HomeFragment(), "HomeFragment")
    }

    private fun createRequest(): Request {
        val wsUrl = "wss://light.techno-dev.com.br"
        return Request.Builder().url(wsUrl).build()
    }

    private fun updateFragmentAnimation(animationFlags: BooleanArray) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (currentFragment) {
            is BassFragment -> currentFragment.updateAnimation(animationFlags[0])
            is MediumFragment -> currentFragment.updateAnimation(animationFlags[1])
            is TrebleFragment -> currentFragment.updateAnimation(animationFlags[2])
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        // webSocket?.cancel()
    }
}
