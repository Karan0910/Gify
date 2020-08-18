package com.company.gify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.gify.db.entities.Gif
import com.company.gify.utils.Event

class FavoriteEventsViewModel : ViewModel() {

    private val _gifFavoriteEvent = MutableLiveData<Event<Gif>>()
    val gifFavoriteEvent: LiveData<Event<Gif>>
        get() = _gifFavoriteEvent

    private val _gifUnfavoriteFromTrendingEvent = MutableLiveData<Event<Gif>>()
    val gifUnfavoriteFromTrendingEvent: LiveData<Event<Gif>>
        get() = _gifUnfavoriteFromTrendingEvent

    private val _gifUnfavoriteFromFavoriteEvent = MutableLiveData<Event<Gif>>()
    val gifUnfavoriteFromFavoriteEvent: LiveData<Event<Gif>>
    get() =  _gifUnfavoriteFromFavoriteEvent

    fun handleGifFavorited(gif: Gif) {
        _gifFavoriteEvent.value = Event(gif)
    }

    fun handleGifUnfavoriteFromTrending(gif: Gif) {
        _gifUnfavoriteFromTrendingEvent.value = Event(gif)
    }

    fun handleGifUnfavoritedFromFavorite(gif: Gif) {
        _gifUnfavoriteFromFavoriteEvent.value = Event(gif)
    }
}