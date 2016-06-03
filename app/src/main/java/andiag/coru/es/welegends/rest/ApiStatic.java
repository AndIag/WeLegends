package andiag.coru.es.welegends.rest;

import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by andyqm on 03/06/2016.
 */
public interface ApiStatic {

    @GET("/champion.json")
    Call<Summoner> getChampions();//TODO Continue this API

}
