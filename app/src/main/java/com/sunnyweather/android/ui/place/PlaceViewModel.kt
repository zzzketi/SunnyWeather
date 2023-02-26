package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    //界面上显示的城市数据进行缓存
    val placeList = ArrayList<Place>()


    val placeLiveData = Transformations.switchMap(searchLiveData){
        Repository.searchPlaces(it)
    }
    fun searchPlaces(query:String){
        searchLiveData.value = query
    }
}