package es.coru.andiag.welegends.models.api.endpoints

import es.coru.andiag.welegends.models.api.dto.GenericStaticData
import es.coru.andiag.welegends.models.database.static_data.*
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Canalejas on 08/12/2016.
 */

interface Static {

    //Profile Icons
    @GET("profileicon.json")
    fun profileIcons(): Call<GenericStaticData<String, ProfileIcon>>

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
