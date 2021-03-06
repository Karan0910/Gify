package com.company.gify.api

import com.company.gify.di.DaggerApiComponent
import com.company.gify.model.GifsResult
import io.reactivex.Single
import javax.inject.Inject

class ApiService {

    @Inject
    lateinit var gifyapi: GifyApi

    companion object {
        const val BASE_URL = "https://api.giphy.com/v1/gifs/"
    }

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun fetchTrendingGifs(api_key: String, limit: Int, offset: Int): Single<GifsResult> {
        return gifyapi.getTrendingGif(api_key, limit, offset)
    }

    fun fetchSearchedGifs(
        api_key: String,
        query: String,
        limit: Int,
        offset: Int
    ): Single<GifsResult> {
        return gifyapi.getSearchedGif(api_key, query, limit, offset)
    }
}