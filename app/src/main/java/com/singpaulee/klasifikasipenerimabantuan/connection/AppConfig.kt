package com.singpaulee.klasifikasipenerimabantuan.connection

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AppConfig {
    fun retrofitConfig(context: Context): Retrofit {

        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/AKfycbxiSBtxY9oHYlAosiXLLAkhu0LGO7wHK-YevbX08H2PI0aNcCmh/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()
    }
}