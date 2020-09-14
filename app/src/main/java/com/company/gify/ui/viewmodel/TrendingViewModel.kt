package com.company.gify.ui.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.gify.BuildConfig
import com.company.gify.api.ApiService
import com.company.gify.db.GifDatabase
import com.company.gify.db.entities.Gif
import com.company.gify.di.DaggerApiComponent
import com.company.gify.model.GifsResult
import com.company.gify.model.mapToGif
import com.company.gify.ui.onItemClickListener
import com.company.gify.utils.Event
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.log

class TrendingViewModel : ViewModel(), onItemClickListener {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    var pageNumber = 0
    var itemCount = 20

    private var dataBaseInstance: GifDatabase? = null

    private val _closeSearchEvent = MutableLiveData<Event<Any>>()
    val closeSearchEvent: LiveData<Event<Any>>
        get() = _closeSearchEvent

    fun setInstanceOfDb(dataBaseInstance: GifDatabase) {
        this.dataBaseInstance = dataBaseInstance
        gifListLD = dataBaseInstance.gifDataDao().getLiveRecords()
    }

    init {
        DaggerApiComponent.create().inject(this)
    }

    lateinit var gifListLD: LiveData<List<Gif>>

    private val inProgress by lazy { MutableLiveData<Boolean>() }
    val inProgressLD: LiveData<Boolean>
        get() = inProgress

    private val belowInProgress by lazy { MutableLiveData<Boolean>() }
    val belowInProgressLD: LiveData<Boolean>
        get() = belowInProgress

    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError

    @SuppressLint("CheckResult")
    fun onRefresh() {

        dataBaseInstance?.gifDataDao()?.removeAllData()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            fetchGifs()
                        }
        // TODO: 13/09/20 Reset database and pagination and make a fresh network call
//        if (isFavoriteListFetched) {
//            refresh()
//        } else {
//            dataBaseInstance?.let { database: GifDatabase ->
//                compositeDisposable.add(database.gifDataDao().getAllRecords()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { favorites, throwable ->
//                        favoriteList = favorites as? ArrayList<Gif> ?: ArrayList()
//                        isFavoriteListFetched = throwable == null
//                        refresh()
//                    })
//            } ?: refresh()
//        }
    }

    private fun refresh() {
        pageNumber = 0
        _closeSearchEvent.value=Event(Any())
        fetchGifs()
    }

    fun handleSearchQuery(query: String?) {
        inProgress.value = true
        pageNumber = 0
        //if (pageNumber == 0)
        fetchGifs()
    }

    fun fetchGifs() {
        if (pageNumber > 0)
            belowInProgress.value = true
        fetchGifData(
            apiService.fetchTrendingGifs(
                BuildConfig.GIFY_API_KEY,
                itemCount,
                pageNumber * itemCount
            )
        )
        //pageNumber++
    }

    private fun fetchGifData(single: Single<GifsResult>) {
        compositeDisposable.add(single.subscribeOn(Schedulers.io())
            .flatMap {
                Observable.fromIterable(it.data)
                    .map { mapToGif(it) }
                    .toList()
            }
            .subscribe({
                //onGifsFetched(it)
                Log.d("TAG", "fetchGifData: "+it)
                dataBaseInstance?.gifDataDao()?.insertGifList(it)

            }, {
                onError(it)
            })
        )
    }

    private fun onGifsFetched(gifs: List<Gif>) {
        isError.value = false
        //setFavoritesOnNetworkPage(gifs)
        //localGifList = addFetchedGifs(gifs as ArrayList)
        inProgress.value = false
        belowInProgress.value = false
    }

  /*  private fun setFavoritesOnNetworkPage(networkGifs: List<Gif>) {
        for (gif in networkGifs) {
            for (favGif in favoriteList) {
                if (favGif.id == gif.id) {
                    gif.isFavorite = true
                }
            }
        }
    }*/

    /*private fun addFetchedGifs(addgifList: ArrayList<Gif>): ArrayList<Gif> {
        val newLocalGifList = ArrayList(localGifList)
        newLocalGifList.addAll(addgifList)
        gifList.value = newLocalGifList
        return newLocalGifList
    }*/

    override fun onItemClick(gif: Gif) {
        if (gif.isFavorite) {
           // removeFavoriteGif(gif)

        } else {
           // insertFavoriteGif(gif)
        }
    }

    /*private fun insertFavoriteGif(gif: Gif) {
        val database = dataBaseInstance ?: return
        gif.isFavorite = true;
        database.gifDataDao().insertGifData(gif)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _gifFavoriteEvent.value = Event(gif)
                favoriteList.add(gif)
                updateFavoriteFlag(gif, true)
            }, {

            }).let {
                compositeDisposable.add(it)
            }
    }*/

    /*private fun removeFavoriteGif(gif: Gif) {
        val database = dataBaseInstance ?: return
        gif.isFavorite = false
        database.gifDataDao().removeGifData(gif)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _gifUnfavoriteEvent.value = Event(gif)
                favoriteList.remove(gif)
                updateFavoriteFlag(gif, false)
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }*/

    /*private fun updateFavoriteFlag(gif: Gif, isFavorite: Boolean) {
        val newGifList = gifListLD.value?.toMutableList() ?: return
        if (newGifList.indexOf(gif) != -1) {
            val updatedGif = newGifList.get(newGifList.indexOf(gif))
            updatedGif.isFavorite = isFavorite
            gifList.value = newGifList
        }

    }*/

    /*fun handleGifUnfavorited(gif: Gif) {
        removeFavoriteGif(gif)
    }*/

    private fun onError(e: Throwable) {
        inProgress.value = false
        isError.value = true
        belowInProgress.value = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}