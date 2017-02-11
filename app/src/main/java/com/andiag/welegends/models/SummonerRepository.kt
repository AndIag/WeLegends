package com.andiag.welegends.models

import android.util.Log
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.models.database.Summoner_Table
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
class SummonerRepository private constructor() : ISummonerRepository {
    private val TAG: String = Summoner::class.java.simpleName

    companion object {
        private var REPOSITORY: ISummonerRepository? = null

        fun getInstance(): ISummonerRepository {
            if (REPOSITORY == null) {
                REPOSITORY = SummonerRepository()
            }
            return REPOSITORY!!
        }

    }

    override fun getSummoner(id: Int): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.mid.eq(id))
                .querySingle()
    }

    override fun getSummoner(name: String, region: String): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.name.eq(name).collate(Collate.NOCASE))
                .and(Summoner_Table.region.eq(region))
                .querySingle()
    }

    override fun loadSummoner(name: String, region: String, callback: Callback<Summoner>) {
        val call = RestClient.getWeLegendsData().getSummonerByName(region, URLEncoder.encode(name, "UTF-8"))
        call.enqueue(callback)
    }

    override fun getSummonerLeagues(callback: Callback<MutableMap<QueueType, QueueStats>>, region: String, id: Long) {
        val call = RestClient.getWeLegendsData().getSummonerDetails(id, region)
        call.enqueue(callback)
    }

    override fun getSummonerHistoric(limit: Int, callback: CallbackData<List<Summoner>?>) {
        callback.onSuccess(SQLite.select().from<Summoner>(Summoner::class.java)
                .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList())
    }

    override fun getSummoner(name: String, region: String, callback: CallbackData<Summoner>) {
        // We look in local database
        val summonerLocal = getSummoner(name, region)
        if (summonerLocal == null){
            loadSummoner(name, region, object : Callback<Summoner>{
                override fun onResponse(call: Call<Summoner>?, response: Response<Summoner>?) {
                    if (response!!.isSuccessful){
                        doAsync {
                            Log.i(TAG, "EPSummoner %d found".format(response.body().riotId))
                            val summonerRemote: Summoner = response.body()
                            summonerRemote.region = region
                            summonerRemote.lastUpdate = Calendar.getInstance().timeInMillis
                            summonerRemote.save()
                            Log.i(TAG, "Saving new summoner %s".format(summonerRemote.name))
                            uiThread {
                                callback.onSuccess(summonerRemote)
                            }
                        }
                        return
                    }
                    callback.onError(null)
                }
                override fun onFailure(call: Call<Summoner>?, t: Throwable?) {
                    callback.onError(t)
                }
            })
        } else {
            Log.i(TAG, "Summoner %s found in database".format(summonerLocal.name))
            summonerLocal.lastUpdate = Calendar.getInstance().timeInMillis
            summonerLocal.saveOrUpdate()
            callback.onSuccess(summonerLocal)
        }
    }

}