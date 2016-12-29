package com.andiag.welegends.models.wrapped.api.endpoints

import com.andiag.welegends.models.wrapped.api.dto.GenericStaticData
import com.andiag.welegends.models.wrapped.database.static_data.*
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Canalejas on 08/12/2016.
 */

interface StaticAPI {

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

    //Summoner Spells
    @GET("summoner.json")
    fun summonerSpells(): Call<GenericStaticData<String, SummonerSpell>>

}
