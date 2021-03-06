package com.company.gify.ui.viewmodel

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

class TrendingViewModel : ViewModel(), onItemClickListener {

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    private var favoriteList = ArrayList<Gif>()

    private var isFavoriteListFetched = false

    private lateinit var localGifList: ArrayList<Gif>

    var pageNumber = 0
    var itemCount = 20

    private var searchQuery = ""

    private var dataBaseInstance: GifDatabase? = null

    private val _gifFavoriteEvent = MutableLiveData<Event<Gif>>()
    val gifFavoriteEvent: LiveData<Event<Gif>>
        get() = _gifFavoriteEvent

    private val _gifUnfavoriteEvent = MutableLiveData<Event<Gif>>()
    val gifUnfavoriteEvent: LiveData<Event<Gif>>
        get() = _gifUnfavoriteEvent

    private val _closeSearchEvent = MutableLiveData<Event<Any>>()
    val closeSearchEvent: LiveData<Event<Any>>
        get() = _closeSearchEvent

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

    fun onRefresh() {
        if (isFavoriteListFetched) {
            refresh()
        } else {
            dataBaseInstance?.let { database: GifDatabase ->
                compositeDisposable.add(database.gifDataDao().getAllRecords()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { favorites, throwable ->
                        favoriteList = favorites as? ArrayList<Gif> ?: ArrayList()
                        isFavoriteListFetched = throwable == null
                        refresh()
                    })
            } ?: refresh()
        }
    }

    private fun refresh() {
        pageNumber = 0
        searchQuery = ""
        _closeSearchEvent.value=Event(Any())
        localGifList = ArrayList()
        fetchGifs()
    }

    fun handleSearchQuery(query: String?) {
        inProgress.value = true
        searchQuery = query ?: ""
        pageNumber = 0
        if (pageNumber == 0)
            localGifList = ArrayList()
        fetchGifs()
    }

    fun fetchGifs() {
        if (pageNumber > 0)
            belowInProgress.value = true
        if (searchQuery.isEmpty()) {

            fetchGifData(
                apiService.fetchTrendingGifs(
                    BuildConfig.GIFY_API_KEY,
                    itemCount,
                    pageNumber * itemCount
                )
            )

        } else {

            fetchGifData(
                apiService.fetchSearchedGifs(
                    BuildConfig.GIFY_API_KEY,
                    searchQuery,
                    itemCount,
                    pageNumber * itemCount
                )
            )

        }
        pageNumber++
    }

    private fun fetchGifData(single: Single<GifsResult>) {
        compositeDisposable.add(single.subscribeOn(Schedulers.io())
            .flatMap {
                Observable.fromIterable(it.data)
                    .map { mapToGif(it) }
                    .toList()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onGifsFetched(it)
            }, {
                onError(it)
            })
        )
    }

    private fun onGifsFetched(gifs: List<Gif>) {
        isError.value = false
        setFavoritesOnNetworkPage(gifs)
        localGifList = addFetchedGifs(gifs as ArrayList)
        inProgress.value = false
        belowInProgress.value = false
    }

    private fun setFavoritesOnNetworkPage(networkGifs: List<Gif>) {
        for (gif in networkGifs) {
            for (favGif in favoriteList) {
                if (favGif.id == gif.id) {
                    gif.isFavorite = true
                }
            }
        }
    }

    private fun addFetchedGifs(addgifList: ArrayList<Gif>): ArrayList<Gif> {
        val newLocalGifList = ArrayList(localGifList)
        newLocalGifList.addAll(addgifList)
        gifList.value = newLocalGifList
        return newLocalGifList
    }

    override fun onItemClick(gif: Gif) {
        if (gif.isFavorite) {
            removeFavoriteGif(gif)

        } else {
            insertFavoriteGif(gif)
        }
    }

    private fun insertFavoriteGif(gif: Gif) {
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
    }

    private fun removeFavoriteGif(gif: Gif) {
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
    }

    private fun updateFavoriteFlag(gif: Gif, isFavorite: Boolean) {
        val newGifList = gifListLD.value?.toMutableList() ?: return
        if (newGifList.indexOf(gif) != -1) {
            val updatedGif = newGifList.get(newGifList.indexOf(gif))
            updatedGif.isFavorite = isFavorite
            gifList.value = newGifList
        }

    }

    fun handleGifUnfavorited(gif: Gif) {
        removeFavoriteGif(gif)
    }

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