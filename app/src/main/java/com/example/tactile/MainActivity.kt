package com.example.tactile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tactile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()  {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding

    var animation: LongArray = longArrayOf(1000,500,250)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.bottomAppBar)

        binding.bottomNavigation.background=null
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottomHome -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottomBass -> {
                    val duration = animation[0] // Set duration for FragmentA
                    replaceFragment(BassFragment.newInstance(duration))
                    true
                }
                R.id.bottomMedium -> {
                    val duration = animation[1] // Set duration for FragmentA
                    replaceFragment(MediumFragment.newInstance(duration))
                    true
                }
                R.id.bottomHigh -> {
                    val duration = animation[2] // Set duration for FragmentA
                    replaceFragment(TrebleFragment.newInstance(duration))
                    true
                }

                else -> false
            }
        }
        fragmentManager = supportFragmentManager
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

}