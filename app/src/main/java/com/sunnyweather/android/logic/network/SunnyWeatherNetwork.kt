package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//上面两个不会自动导包怎么回事？？？

object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    //最后直接调用这个就可以，返回值也是response
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    //await是函数名
    //注意协程阻塞问题
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) it.resume(body)
                    else it.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }

            })
        }


    }
}