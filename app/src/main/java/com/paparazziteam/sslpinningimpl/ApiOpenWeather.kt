package com.paparazziteam.sslpinningimpl

import retrofit2.Call
import retrofit2.http.GET

interface ApiOpenWeather {

    @GET("guide")
    fun getData(): Call<String>
}