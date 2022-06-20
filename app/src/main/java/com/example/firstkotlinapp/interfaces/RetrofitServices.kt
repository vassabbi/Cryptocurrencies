package com.example.firstkotlinapp.interfaces

import com.example.firstkotlinapp.model.Crypto
import com.example.firstkotlinapp.model.History
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitServices{
    @GET("assets/{id}")
    fun getCrypto(@Path("id") id : String): Call<Crypto>

    @GET("assets/{id}/history?interval=d1")
    fun getCryptoHistory(@Path("id") id : String) : Call<History>
}