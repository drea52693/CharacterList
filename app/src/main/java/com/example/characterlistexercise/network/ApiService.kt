package com.example.characterlistexercise.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // update this to fetch ANY movie
    @GET("/?q=simpsons+characters&format=json")
    fun getCharacters(): Call<CharacterResponse>

    @GET("/i/4d663a85.gif")
    fun getImage(): Call<String>

}