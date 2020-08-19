package com.company.gify.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.gify.db.GifDatabase
import com.company.gify.db.entities.Gif
import com.company.gify.ui.onItemClickListener
import com.company.gify.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouriteViewModel : ViewModel(), onItemClickListener {

    protected val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: GifDatabase? = null

    private val _gifUnfavoriteEvent = MutableLiveData<Event<Gif>>()
    val gifUnfavoriteEvent: LiveData<Event<Gif>>
        get() = _gifUnfavoriteEvent

    private val gifList by lazy { MutableLiveData<List<Gif>>() }

    val gifListLD: LiveData<List<Gif>>
        get() = gifList

    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError

    fun setInstanceOfDb(dataBaseInstance: GifDatabase) {
        this.dataBaseInstance = dataBaseInstance
    }

    fun getGifData() {
        val database = dataBaseInstance ?: return
        database.gifDataDao().getAllRecords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    gifList.postValue(it)
                    isError.postValue(false)
                } else {
                    isError.postValue(true)
                    gifList.postValue(listOf())
                }
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    override fun onItemClick(gif: Gif) {
        _gifUnfavoriteEvent.value = Event(gif)
    }

    fun handleGifFavorited(gif: Gif) {
        val newFavGifList = gifListLD.value?.toMutableList() ?: ArrayList()
        newFavGifList.add(gif)
        if (newFavGifList.size > 0)
            isError.postValue(false)
        gifList.value = newFavGifList
    }

    fun handleGifUnfavorited(gif: Gif) {
        val newFavGifList = gifListLD.value?.toMutableList() ?: return
        newFavGifList.remove(gif)
        if (newFavGifList.size == 0)
            isError.postValue(true)
        gifList.value = newFavGifList
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}