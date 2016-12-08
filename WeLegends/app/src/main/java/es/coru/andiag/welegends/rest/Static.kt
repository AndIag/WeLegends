package es.coru.andiag.welegends.rest

import es.coru.andiag.welegends.entities.Summoner
import es.coru.andiag.welegends.entities.dto.GenericStaticData
import es.coru.andiag.welegends.entities.persist.Champion
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Canalejas on 08/12/2016.
 */

interface Static {

    //Profile Icons
    @GET("profileicon.json")
    fun profileIcons(): Call<Summoner>

    //Champions
    @GET("champion.json")
    fun champions(): Call<GenericStaticData<String, Champion>>

    //Items
    @GET("item.json")
    fun items(): Call<Summoner>

    //Masteries
    @GET("mastery.json")
    fun masteries(): Call<Summoner>

    //Runes
    @GET("rune.json")
    fun runes(): Call<Summoner>

    //Summoner Spells
    @GET("summoner.json")
    fun summonerSpells(): Call<Summoner>

}
