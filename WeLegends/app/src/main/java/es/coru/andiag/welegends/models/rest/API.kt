package es.coru.andiag.welegends.models.rest

import es.coru.andiag.welegends.models.entities.database.Summoner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Canalejas on 08/12/2016.
 */

interface API {

    @GET("{region}/summoner/{name}")
    fun getSummonerByName(@Path("region") region: String, @Path("name") name: String): Call<Summoner>

    @GET("versions")
    fun getServerVersion(): Call<List<String>>

}
