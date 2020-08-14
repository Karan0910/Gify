package com.company.gify.ui.activity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.company.gify.R
import com.company.gify.databinding.ActivityMainBinding
import com.company.gify.ui.adapter.ViewPagerAdapter
import com.company.gify.viewmodel.TrendingViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        init();
        setContentView(binding.root)
    }

    private fun init(){
        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter=viewPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchView = menu?.findItem(R.id.searchView)?.actionView as SearchView
        onPageChange()
        return true
    }

    private fun onPageChange(){
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

           override fun onPageScrollStateChanged(state: Int) {

           }

           override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

           }
           override fun onPageSelected(position: Int) {
               Log.d("TAG", "onPageSelected: "+position)
               if(position==0)
                   searchView.visibility= View.VISIBLE
               else if(position==1)
                   searchView.visibility= View.GONE
           }

       })
    }

}