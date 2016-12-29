package com.andiag.welegends.models

import android.util.Log
import com.andiag.libraryutils.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.wrapped.api.RestClient
import com.andiag.welegends.models.wrapped.database.Summoner
import com.andiag.welegends.models.wrapped.database.Summoner_Table
import com.andiag.welegends.presenters.summoners.PresenterSummonerLoader
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
        if (!name.isEmpty()) {
            // Try to find summoner in local database
            var summoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                    .where(Summoner_Table.name.eq(name).collate(Collate.NOCASE))
                    .and(Summoner_Table.region.eq(region))
                    .querySingle()
            if (summoner == null) {
                // If summoner is not in local search it in server
                val cleanName = URLEncoder.encode(name, "UTF-8")
                val call = RestClient.getWeLegendsData().getSummonerByName(region, cleanName)
                call.enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                        if (response.isSuccessful) {
                            doAsync {
                                Log.d(TAG, "Summoner %d found".format(response.body().riotId))
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
     * @param [caller] contains error and success callbacks
     * @param [limit] max number of elements returned
     * @return [MutableList] of [Summoner]
     */
    fun getSummonerHistoric(caller: AIInterfaceLoaderHandlerPresenter<List<Summoner>>, limit: Int) {
        return caller.onLoadSuccess(SQLite.select().from<Summoner>(Summoner::class.java)
                .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList())
    }

}