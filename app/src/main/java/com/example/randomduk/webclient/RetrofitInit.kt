package com.example.randomduk.webclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class RetrofitInit() {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://random-d.uk/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(DukService::class.java)
}