package com.company.gify.di

import androidx.fragment.app.Fragment
import com.company.gify.api.ApiService
import com.company.gify.ui.fragment.TrendingFragment
import com.company.gify.viewmodel.TrendingViewModel
import dagger.Component


@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(apiService: ApiService)

    fun inject(trendingViewModel: TrendingViewModel)

    fun inject(trendingFragment: TrendingFragment)
}