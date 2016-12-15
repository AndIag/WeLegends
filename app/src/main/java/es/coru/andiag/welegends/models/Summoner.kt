package es.coru.andiag.welegends.models

import android.util.Log
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner_Table
import es.coru.andiag.welegends.presenters.summoners.PresenterSummonerLoader
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.util.*

/**
 * Created by Canalejas on 14/12/2016.
 */
object Summoner {
    private val TAG: String = Summoner::class.java.simpleName

    /**
     * Try to find a [Summoner] in local database or in riot server
     * @param [name]
     * @param [region]
     * @return [PresenterSummonerLoader.onSummonerFound] with [Summoner] data
     *      or call [PresenterSummonerLoader.onSummonerLoadError]
     */
    fun getSummonerByName(caller: PresenterSummonerLoader, name: String, region: String) {
        val cleanName = URLEncoder.encode(name, "UTF-8")
        if (!cleanName.isEmpty()) {
            var summoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                    .where(Summoner_Table.name.eq(name))
                    .and(Summoner_Table.region.eq(region))
                    .querySingle()
            if (summoner == null) {
                val call = RestClient.getWeLegendsData().getSummonerByName(region, cleanName)
                call.enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                        if (response.isSuccessful) {
                            doAsync {
                                Log.d(TAG, response.body().toString())
                                summoner = response.body()
                                summoner!!.region = region
                                summoner!!.lastUpdate = Calendar.getInstance().timeInMillis
                                summoner!!.save()
                                uiThread {
                                    Log.i(TAG, "Saving new summoner %s".format(summoner!!.name))
                                    caller.onSummonerFound(summoner!!)
                                }
                            }
                            return
                        }
                        Log.e(TAG, "Error %s loading summoner".format(response.message()))
                        caller.onSummonerLoadError(response.message())
                    }

                    override fun onFailure(call: Call<Summoner>, t: Throwable) {
                        Log.e(TAG, "Error %s loading summoner".format(t.message))
                        caller.onSummonerLoadError(R.string.error404)
                    }
                })
            } else {
                Log.i(TAG, "Summoner %s found in database".format(summoner!!.name))
                caller.onSummonerFound(summoner!!)
            }
        } else {
            Log.e(TAG, "Empty summoner")
            caller.onSummonerLoadError(R.string.voidSummonerError)
        }
    }

    /**
     * Return a list of summoners order by date DESC
     * @param [limit] max number of elements returned
     * @return [MutableList] of [Summoner]
     */
    fun getSummonerHistoric(caller: AIInterfaceLoaderPresenter<List<Summoner>>, limit: Int) {
        return caller.onLoadSuccess("Success", SQLite.select().from<Summoner>(Summoner::class.java)
                .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList())
    }

}