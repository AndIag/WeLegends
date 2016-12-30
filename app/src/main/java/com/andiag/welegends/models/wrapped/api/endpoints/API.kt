package com.andiag.welegends.models.wrapped.api.endpoints

import com.andiag.welegends.models.wrapped.database.Summoner
import com.andiag.welegends.models.wrapped.database.ranked.QueueStats
import com.andiag.welegends.models.wrapped.database.ranked.QueueType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Canalejas on 08/12/2016.
 */

interface API {

    @GET("summoners")
    fun getSummonerByName(@Query("region") region: String, @Query("summoner_name") name: String): Call<Summoner>

    @GET("summoners/{id}")
    fun getSummonerDetails(@Path("id") id: Long, @Query("region") region: String): Call<Map<QueueType, QueueStats>>

}
