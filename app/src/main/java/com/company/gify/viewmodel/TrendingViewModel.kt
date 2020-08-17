package com.company.gify.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.gify.api.ApiService
import com.company.gify.db.GifDatabase
import com.company.gify.db.entities.GifData
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

    //assuming this list will contain data
    lateinit var favoriteList: ArrayList<Gif>

    private lateinit var localGifList : ArrayList<Gif>

    var pageNumber =0
    var itemCount=20

    private var dataBaseInstance: GifDatabase ?= null

    fun setInstanceOfDb(dataBaseInstance: GifDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }


    init {
        DaggerApiComponent.create().inject(this)

    }

    private val gifList by lazy { MutableLiveData<List<Gif>>() }



    val gifListLD: LiveData<List<Gif>>
        get() = gifList


    private val inProgress by lazy { MutableLiveData<Boolean>() }
    val inProgressLD: LiveData<Boolean>
        get() = inProgress

    private val belowInProgress by lazy { MutableLiveData<Boolean>() }
    val belowInProgressLD: LiveData<Boolean>
        get() = belowInProgress

    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError


    fun onRefresh(){
        pageNumber=0
        localGifList= ArrayList()
        fetchGifs()
    }

    fun fetchGifs() {

        if(pageNumber>0)
            belowInProgress.value=true

        compositeDisposable.add(
            apiService.fetchTrendingGifs("g6OYYyLwvtQiDL78sg877bwLmGVQd6L9",itemCount,(pageNumber*itemCount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.data }
                .subscribeWith(createGifObserver())
        )

        pageNumber++
    }

    fun fetchSearchedGifs(query : String) {
        compositeDisposable.add(
            apiService.fetchSearchedGifs("g6OYYyLwvtQiDL78sg877bwLmGVQd6L9",query,itemCount,pageNumber*itemCount)
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
                gifList.value = addFav(gifs)
                localGifList= addFetchedGifs(gifs as ArrayList)
                inProgress.value = false
                belowInProgress.value=false
            }

            override fun onError(e: Throwable) {
                inProgress.value = true
                isError.value = true
                Log.e("onError()", "Error: ${e.message}")
                inProgress.value = false
                belowInProgress.value=false
            }
        }
    }

    fun addRemoveGif(gif: Gif){
        for( newGif in localGifList){
            if (newGif.id==gif.id){
                newGif.favorite = !gif.favorite
                if(newGif.favorite){
                    //favoriteList.add(newGif)
                    var gifData =GifData(imageId = newGif.id,imageURL = newGif.images.preview_gif.url)
                    saveDataIntoDb(gifData)
                } else {
                    //favoriteList.remove(newGif)
                }

            }
        }
        gifList.value=localGifList
    }


    // add fav gifs in adapter
    fun addFav(gifList : List<Gif>) : List<Gif> {



        // change fav list
        favoriteList = gifList as ArrayList<Gif>

        if(favoriteList.size==0) {
        for (gif in gifList){

                 for(favGif in favoriteList){

                     if(favGif.id.equals(gif.id)){
                         gif.favorite=true
                     }
                 }
         }
        }
        return gifList
    }

    fun addFetchedGifs(addgifList : ArrayList<Gif>) : ArrayList<Gif> {

        localGifList.addAll(addgifList)
        gifList.value=localGifList
        return localGifList
    }


    fun saveDataIntoDb(data: GifData){

        dataBaseInstance?.gifDataDao()?.insertGifData(data)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }



}