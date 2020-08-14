package com.company.gify.di

import com.company.gify.api.ApiService
import com.company.gify.api.GifyApi
import com.company.gify.model.Gif
import com.company.gify.ui.adapter.TrendingGifAdapter
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    fun provideGifyApi(): GifyApi {
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GifyApi::class.java)
    }

    @Provides
    fun provideNetworkService(): ApiService {
        return ApiService()
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }


    @Provides
    fun provideGifList(): ArrayList<Gif> {
        return ArrayList()
    }

    @Provides
    fun provideGifAdapter(vehicles: ArrayList<Gif>): TrendingGifAdapter {
        return TrendingGifAdapter(vehicles)
    }
}