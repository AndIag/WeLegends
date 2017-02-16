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
import java.net.HttpURLConnection
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

    override fun read(id: Int, mode: LoadingType, callback: CallbackData<Summoner>) {
        when (mode) {
            LoadingType.LOCAL_LOAD -> {
                doAsync {
                    val summoner: Summoner? = onLocalLoadSummoner(id)
                    uiThread {
                        callback.onSuccess(summoner)
                    }
                }
            }
            else -> {
                throw UnsupportedOperationException(mode.name)
            }
        }
    }

    override fun read(name: String, region: String, mode: LoadingType, callback: CallbackData<Summoner>) {
        when (mode) {
            LoadingType.LOCAL_LOAD -> {
                doAsync {
                    val summoner: Summoner? = onLocalLoadSummoner(name, region)
                    uiThread {
                        callback.onSuccess(summoner)
                    }
                }
            }
            LoadingType.REMOTE_LOAD -> {
                onRemoteLoadSummoner(name, region, callback)
            }
            else -> {
                throw UnsupportedOperationException(mode.name)
            }
        }
    }

    override fun getSummonerLeagues(id: Long, region: String, callback: CallbackData<MutableMap<QueueType, QueueStats>>) {
        val call = RestClient.getWeLegendsData().getSummonerDetails(id, region)
        call.enqueue(object : Callback<MutableMap<QueueType, QueueStats>> {

            override fun onResponse(call: Call<MutableMap<QueueType, QueueStats>>?,
                                    response: Response<MutableMap<QueueType, QueueStats>>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "Leagues loaded for summoner %s".format(id))
                    callback.onSuccess(response.body())
                    return
                }
                if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // If summoner has no queue entries
                    Log.i(TAG, "No leagues found for summoner %s".format(id))
                    callback.onSuccess(HashMap<QueueType, QueueStats>())
                    return
                }
                Log.e(TAG, "Error %s loading summoner leagues".format(response.message()))
                callback.onError(Throwable(response.message()))
            }

            override fun onFailure(call: Call<MutableMap<QueueType, QueueStats>>?, t: Throwable) {
                Log.e(TAG, "Error %s loading summoner details".format(t.message))
                callback.onError(t)
            }

        })
    }

    override fun getSummonerHistoric(limit: Int, callback: CallbackData<List<Summoner>>) {
        doAsync {
            val summonerList: List<Summoner> = SQLite.select().from<Summoner>(Summoner::class.java)
                    .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList()
            uiThread {
                callback.onSuccess(summonerList)
            }
        }
    }

    private fun onLocalLoadSummoner(id: Int): Summoner? {
        val summoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.mid.eq(id))
                .querySingle()

        if (summoner != null) {
            summoner.lastUpdate = Calendar.getInstance().timeInMillis
            summoner.saveOrUpdate()
        }

        return summoner
    }

    private fun onLocalLoadSummoner(name: String, region: String): Summoner? {
        val summoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.name.eq(name).collate(Collate.NOCASE))
                .and(Summoner_Table.region.eq(region))
                .querySingle()

        if (summoner != null) {
            summoner.lastUpdate = Calendar.getInstance().timeInMillis
            summoner.saveOrUpdate()
        }

        return summoner
    }

    private fun onRemoteLoadSummoner(name: String, region: String, callback: CallbackData<Summoner>) {
        RestClient.getWeLegendsData()
                .getSummonerByName(region, URLEncoder.encode(name, "UTF-8"))
                .enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>?, response: Response<Summoner>?) {
                        if (response!!.isSuccessful) {
                            doAsync {
                                Log.i(TAG, "Summoner %d found".format(response.body().riotId))
                                val summoner: Summoner = response.body()
                                summoner.region = region
                                summoner.lastUpdate = Calendar.getInstance().timeInMillis
                                summoner.saveOrUpdate()
                                Log.i(TAG, "Saving summoner %s".format(summoner.name))
                                uiThread {
                                    callback.onSuccess(summoner)
                                }
                            }
                            return
                        }
                        callback.onError(null)
                    }

                    override fun onFailure(call: Call<Summoner>?, t: Throwable?) {
                        Log.e(TAG, t!!.message)
                        callback.onError(t)
                    }
                })
    }

}