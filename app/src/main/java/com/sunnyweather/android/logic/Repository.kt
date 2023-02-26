package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    //此处获得请求返回的结果
    fun searchPlaces(query:String) = liveData(Dispatchers.IO){
        val result = try {
            //此处response就是网络请求的结果
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                //kotlin内置的sucess()方法
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        //类似于调用LiveData的setVlue方法来通知数据变化
        emit(result)
    }
}