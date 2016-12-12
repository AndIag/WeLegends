package es.coru.andiag.welegends.models.rest

import es.coru.andiag.welegends.models.database.Champion
import es.coru.andiag.welegends.models.database.Item
import es.coru.andiag.welegends.models.database.ProfileIcon
import es.coru.andiag.welegends.models.database.Summoner
import es.coru.andiag.welegends.models.dto.GenericStaticData
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
    fun masteries(): Call<Summoner>

    //Runes
    @GET("rune.json")
    fun runes(): Call<Summoner>

    //Summoner Spells
    @GET("summoner.json")
    fun summonerSpells(): Call<Summoner>

}
