package com.company.gify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.company.gify.db.GifDatabase
import com.company.gify.db.entities.GifData
import com.company.gify.model.Gif
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouriteViewModel :ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    private var dataBaseInstance: GifDatabase ?= null

    var gifList = MutableLiveData<List<GifData>>()

    fun setInstanceOfDb(dataBaseInstance: GifDatabase) {
        this.dataBaseInstance = dataBaseInstance
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

    fun getGifData(){

        dataBaseInstance?.gifDataDao()?.getAllRecords()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                if(!it.isNullOrEmpty()){
                    gifList.postValue(it)
                }else{
                    gifList.postValue(listOf())
                }
                it?.forEach {
                    Log.v("gif id",it.imageId)
                }
            },{
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    fun removeGif(person: GifData) {
        dataBaseInstance?.gifDataDao()?.removeGifData(person)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({
                //Refresh Page data
                getGifData()
            },{

            })?.let {
                compositeDisposable.add(it)
            }
    }

}