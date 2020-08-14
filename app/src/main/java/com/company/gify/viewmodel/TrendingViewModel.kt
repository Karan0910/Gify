package com.company.gify.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.gify.api.ApiService
import com.company.gify.di.DaggerApiComponent
import com.company.gify.model.Gif
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TrendingViewModel : ViewModel() {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var compositeDisposable: CompositeDisposable


    init {
        DaggerApiComponent.create().inject(this)
    }

    private val gifList by lazy { MutableLiveData<List<Gif>>() }


    val gifListLD: LiveData<List<Gif>>
        get() = gifList


    private val inProgress by lazy { MutableLiveData<Boolean>() }
    val inProgressLD: LiveData<Boolean>
        get() = inProgress

    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError


    fun onRefresh(){
        fetchGifs()
    }

    private fun fetchGifs() {
        compositeDisposable.add(
            apiService.fetchTrendingGifs("g6OYYyLwvtQiDL78sg877bwLmGVQd6L9")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data }
                .subscribeWith(createGifObserver())
        )
    }

    fun fetchSearchedGifs(query : String) {
        compositeDisposable.add(
            apiService.fetchSearchedGifs("g6OYYyLwvtQiDL78sg877bwLmGVQd6L9",query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data }
                .subscribeWith(createGifObserver())
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun createGifObserver(): DisposableSingleObserver<List<Gif>> {
        return object : DisposableSingleObserver<List<Gif>>() {

            override fun onSuccess(gifs: List<Gif>) {
                inProgress.value = true
                isError.value = false
                gifList.value = gifs
                inProgress.value = false
            }

            override fun onError(e: Throwable) {
                inProgress.value = true
                isError.value = true
                Log.e("onError()", "Error: ${e.message}")
                inProgress.value = false
            }
        }
    }

    fun addGif(gif: Gif){

        Log.d("TAG", "addGif: ")
    }
}