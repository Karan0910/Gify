package com.company.gify.api

import com.company.gify.model.GifsResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GifyApi {
    @GET("trending")
    fun getTrendingGif(
        @Query("api_key") api_key: String?, @Query("limit") limit: Int, @Query("offset")
        offset: Int
    ): Single<GifsResult>


    @GET("search")
    fun getSearchedGif(
        @Query("api_key") api_key: String?,
        @Query("q") query: String?,
        @Query("limit") limit: Int,
        @Query("offset")
        offset: Int
    ): Single<GifsResult>
}