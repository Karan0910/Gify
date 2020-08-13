package com.company.gify.api

import com.company.gify.di.DaggerApiComponent
import com.company.gify.model.GifsResult
import io.reactivex.Single
import javax.inject.Inject

class ApiService {

    @Inject
    lateinit var gifyapi: GifyApi

    companion object {
        val BASE_URL = "https://api.giphy.com/v1/gifs/"
    }


    init {
        DaggerApiComponent.create().inject(this)
    }


    fun fetchTrendingGifs(api_key: String): Single<GifsResult> {
        return gifyapi.getTrendingGif(api_key)
    }
}