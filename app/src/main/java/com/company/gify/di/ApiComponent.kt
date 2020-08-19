package com.company.gify.di

import com.company.gify.api.ApiService
import com.company.gify.ui.fragment.FavouriteFragment
import com.company.gify.ui.fragment.TrendingFragment
import com.company.gify.ui.viewmodel.TrendingViewModel
import dagger.Component


@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(apiService: ApiService)

    fun inject(trendingViewModel: TrendingViewModel)

    fun inject(trendingFragment: TrendingFragment)

    fun inject(favouriteFragment: FavouriteFragment)
}