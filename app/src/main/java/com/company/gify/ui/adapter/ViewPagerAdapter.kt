package com.company.gify.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.company.gify.R
import com.company.gify.ui.fragment.FavouriteFragment
import com.company.gify.ui.fragment.TrendingFragment

class ViewPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_trending,
        R.string.tab_fav
    )
    override fun getItem(position: Int): Fragment {
        when (position) {
            1 -> return TrendingFragment()
            2 -> return FavouriteFragment()
            else -> {
                return TrendingFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}