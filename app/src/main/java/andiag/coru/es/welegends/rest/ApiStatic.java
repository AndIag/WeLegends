package andiag.coru.es.welegends.rest;

import andiag.coru.es.welegends.rest.entities.Summoner;
import andiag.coru.es.welegends.rest.entities.persist.Champion;
import andiag.coru.es.welegends.rest.utils.GenericStaticData;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andyqm on 03/06/2016.
 */
public interface ApiStatic {

    //TODO Continue this API

    //Profile Icons
    @GET("profileicon.json")
    Call<Summoner> getProfileIcons();

    //Champions
    @GET("champion.json")
    Call<GenericStaticData<String, Champion>> getChampions();

    //Items
    @GET("item.json")
    Call<Summoner> getItems();

    //Masteries
    @GET("mastery.json")
    Call<Summoner> getMasteries();

    //Runes
    @GET("rune.json")
    Call<Summoner> getRunes();

    //Summoner Spells
    @GET("summoner.json")
    Call<Summoner> getSummonerSpells();
}
