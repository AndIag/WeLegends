package com.andiag.welegends.models

import android.util.Log
import com.andiag.commons.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.models.entities.Summoner_Table
import com.andiag.welegends.models.entities.league.QueueStats
import com.andiag.welegends.models.entities.league.QueueType
import com.andiag.welegends.presenters.commons.PresenterSummonerLoader
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.sql.language.SQLite
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
object EPSummoner {
    private val TAG: String = Summoner::class.java.simpleName

    /**
     * Try to find a [Summoner] in local database or in riot server
     * @param [name]
     * @param [region]
     * @return [PresenterSummonerLoader.onSummonerFound] with [EPSummoner] data
     *      or call [PresenterSummonerLoader.onSummonerLoadError]
     */
    fun getSummonerByName(caller: PresenterSummonerLoader, name: String, region: String) {
        if (!name.isEmpty()) {
            // Try to find summoner in local database
            val summoner: Summoner? = getLocalSummonerByName(name, region)
            if (summoner != null) {
                Log.i(TAG, "EPSummoner %s found in database".format(summoner.name))
                // Update lastUpdate param
                summoner.lastUpdate = Calendar.getInstance().timeInMillis
                summoner.save()
                caller.onSummonerFound(summoner)
                return
            }
            // If summoner is not in local search it in server
            getRiotSummonerByName(caller, name, region)
        } else {
            Log.e(TAG, "Empty summoner")
            caller.onSummonerLoadError(R.string.voidSummonerError)
        }
    }

    fun getSummonerDetails(caller: AIInterfaceLoaderHandlerPresenter<Map<QueueType, QueueStats>>, region: String, id: Long) {
        val call = RestClient.getWeLegendsData().getSummonerDetails(id, region)
        call.enqueue(object : Callback<Map<QueueType, QueueStats>> {

            override fun onResponse(call: Call<Map<QueueType, QueueStats>>?, response: Response<Map<QueueType, QueueStats>>) {
                if (response.isSuccessful) {
                    Log.d(TAG, response.body().toString())
                    return
                }
                Log.e(TAG, "Error %s loading summoner detials".format(response.message()))
                caller.onLoadError(response.message())
            }

            override fun onFailure(call: Call<Map<QueueType, QueueStats>>?, t: Throwable) {
                Log.e(TAG, "Error %s loading summoner details".format(t.message))
                caller.onLoadError(R.string.error404)
            }

        })
    }

    /**
     * Try to find a [Summoner] in local database
     * @param [name]
     * @param [region]
     * @return [Summoner] or null
     */
    fun getLocalSummonerByName(name: String, region: String): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.name.eq(name).collate(Collate.NOCASE))
                .and(Summoner_Table.region.eq(region))
                .querySingle()
    }

    /**
     * Try to find a [Summoner] in riot database
     * @param [name]
     * @param [region]
     * @return [Summoner] or null
     */
    fun getRiotSummonerByName(caller: PresenterSummonerLoader, name: String, region: String) {
        val call = RestClient.getWeLegendsData().getSummonerByName(region, URLEncoder.encode(name, "UTF-8"))
        call.enqueue(object : Callback<Summoner> {
            override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                if (response.isSuccessful) {
                    doAsync {
                        Log.i(TAG, "EPSummoner %d found".format(response.body().riotId))
                        val summoner: Summoner? = response.body()
                        summoner!!.region = region
                        summoner.lastUpdate = Calendar.getInstance().timeInMillis
                        summoner.save()
                        Log.i(TAG, "Saving new summoner %s".format(summoner.name))
                        uiThread {
                            caller.onSummonerFound(summoner)
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
    }

    /**
     * Try to find a summoner in local database
     * @param [id] summoner id
     * @return [Summoner] or null
     */
    fun getLocalSummonerById(id: Int): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.mid.eq(id))
                .querySingle()
    }

    /**
     * Return a list of summoners order by date DESC
     * @param [caller] contains error and success callbacks
     * @param [limit] max number of elements returned
     * @return [MutableList] of [EPSummoner]
     */
    fun getSummonerHistoric(caller: AIInterfaceLoaderHandlerPresenter<List<Summoner>>, limit: Int) {
        caller.onLoadSuccess(SQLite.select().from<Summoner>(Summoner::class.java)
                .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList())
    }

}