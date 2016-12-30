package com.andiag.welegends.models.api.endpoints

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Canalejas on 15/12/2016.
 */
interface VersionAPI {

    @GET("versions.json")
    fun versions(): Call<List<String>>

}