package com.andiag.welegends.models.api

import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.models.database.static_data.*
import com.andiag.welegends.models.utils.dto.GenericStaticData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Canalejas on 11/02/2017.
 */
object API {

    interface Version {

        @GET("versions.json")
        fun versions(): Call<List<String>>

    }

    interface Static {

        //Champions
        @GET("champion.json")
        fun champions(): Call<GenericStaticData<String, Champion>>

        //Items
        @GET("item.json")
        fun items(): Call<GenericStaticData<String, Item>>

        //Masteries
        @GET("mastery.json")
        fun masteries(): Call<GenericStaticData<String, Mastery>>

        //Runes
        @GET("rune.json")
        fun runes(): Call<GenericStaticData<String, Rune>>

        //EPSummoner Spells
        @GET("summoner.json")
        fun summonerSpells(): Call<GenericStaticData<String, SummonerSpell>>

    }

    interface Riot {

        @GET("summoners")
        fun getSummonerByName(@Query("region") region: String, @Query("summoner_name") name: String): Call<Summoner>

        @GET("summoners/{id}")
        fun getSummonerDetails(@Path("id") id: Long, @Query("region") region: String): Call<MutableMap<QueueType, QueueStats>>

    }

}