package com.company.gify.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.company.gify.R
import com.company.gify.databinding.ActivityMainBinding
import com.company.gify.ui.adapter.ViewPagerAdapter
import com.company.gify.viewmodel.TrendingViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        init();
        setContentView(binding.root)
    }

    fun init(){
        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter=viewPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

}