package andiag.coru.es.welegends.rest;

import java.util.List;

import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Canalejas on 31/05/2016.
 */
public interface Api {

    @GET("{region}/summoner/{name}")
    Call<Summoner> getSummonerByName(@Path("region") String region, @Path("name") String name);

    @GET("versions")
    Call<List<String>> getServerVersion();

}
