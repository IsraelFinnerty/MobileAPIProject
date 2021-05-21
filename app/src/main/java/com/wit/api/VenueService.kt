package com.wit.api

import android.icu.util.TimeUnit
import com.google.gson.GsonBuilder
import com.wit.venues.models.VenueModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.xml.datatype.DatatypeConstants.SECONDS

interface VenueService {
    @GET("api/venues")
    fun getall(): Call<List<VenueModel>>

    @GET("/venues/{id}")
    fun get(@Path("id") id: String): Call<VenueModel>

    @DELETE("/venues/{id}")
    fun delete(@Path("id") id: String): Call<VenueWrapper>

    @POST("/venues")
    fun post(@Body venue: VenueModel): Call<VenueWrapper>


    @PUT("/venues/{id}")
    fun put(@Path("id") id: String,
            @Body venue: VenueModel
    ): Call<VenueWrapper>

    companion object {

        val serviceURL = "https://poi-api-rhythm.herokuapp.com"

        fun create() : VenueService {

            val gson = GsonBuilder().create()

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(VenueService::class.java)
        }
    }
}