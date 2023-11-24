package com.ligalight.tactile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ligalight.tactile.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MainActivity : AppCompatActivity(){

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketListener: WebSocketListener
    private lateinit var viewModel: MainViewModel

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    var animationValues: LongArray = longArrayOf(1000, 500, 250)
    var DEFAULT_DURATION = 666L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = WebSocketListener(viewModel)

        viewModel.animationValues.observe(this, Observer {
                onNewAnimationValues(it)
        })
        
        webSocket=okHttpClient.newWebSocket(createRequest(), webSocketListener)

        setSupportActionBar(binding.bottomAppBar)

        binding.bottomNavigation.background=null
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomHome -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottomBass -> {
                    val duration = animationValues[0]
                    replaceFragment(BassFragment.newInstance(duration))
                    true
                }
                R.id.bottomMedium -> {
                    val duration = animationValues[1]
                    replaceFragment(MediumFragment.newInstance(duration))
                    true
                }
                R.id.bottomHigh -> {
                    val duration = animationValues[2]
                    replaceFragment(TrebleFragment.newInstance(duration))
                    true
                }

                else -> false
            }
        }
        fragmentManager = supportFragmentManager
        replaceFragment(HomeFragment())
    }

    private fun createRequest(): Request {
        val wsUrl = "wss://light.techno-dev.com.br"
        return Request.Builder().url(wsUrl).build()
    }

    // WebSocketListener method to receive updated animation values
    fun onNewAnimationValues(values: LongArray) {
        //viewModel.setAnimationValues(values)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        when (currentFragment) {
            is BassFragment -> currentFragment.updateAnimationDuration( values.getOrNull(0) ?: DEFAULT_DURATION)
            is MediumFragment -> currentFragment.updateAnimationDuration(values.getOrNull(1) ?: DEFAULT_DURATION)
            is TrebleFragment -> currentFragment.updateAnimationDuration(values.getOrNull(2) ?: DEFAULT_DURATION)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        //webSocket.cancel()
    }

}