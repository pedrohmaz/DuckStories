package com.example.randomduk.webclient

import retrofit2.Call
import retrofit2.http.GET

interface DukService {

    @GET("random")
    fun callDuck() : Call<DuckResponse>

}