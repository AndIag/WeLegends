package com.andiag.welegends.views.main

import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.ISummonerRepository
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.IPresenterSummonerLoader
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterSummonerStats(summonersRepository: ISummonerRepository) : AIPresenter<ActivityMain, FragmentSummonerStats>(), IPresenterSummonerLoader, AIInterfaceLoaderHandlerPresenter<MutableMap<QueueType, QueueStats>> {
    private val TAG: String = PresenterSummonerStats::class.java.simpleName
    private val SUMMONER_REPOSITORY: ISummonerRepository = summonersRepository

    private var mSummonerId: Int? = null
    private var mSummonerRiotId: Long? = null

    private var mSummoner: Summoner? = null
    private var mLeagues: MutableMap<QueueType, QueueStats>? = null
    private var leaguesLoaded: Boolean = false

    constructor() : this(SummonerRepository.getInstance())

    private fun handleResponse(response: Response<Summoner>, region: String) {
        if (response.isSuccessful) {
            doAsync {
                Log.i(TAG, "EPSummoner %d found".format(response.body().riotId))
                val summoner: Summoner = response.body()
                summoner.region = region
                summoner.lastUpdate = Calendar.getInstance().timeInMillis
                summoner.save()
                Log.i(TAG, "Saving new summoner %s".format(summoner.name))
                uiThread {
                    onSummonerFound(summoner, false)
                }
            }
            return
        }
        Log.e(TAG, "Error %s loading summoner".format(response.message()))
        onSummonerLoadError(response.message())
    }

    private fun loadSummoner(summonerRiotId: Long, region: String?, name: String?) {
        Log.i(TAG, "Loading Summoner %s from server".format(summonerRiotId))
        SUMMONER_REPOSITORY.loadSummoner(name!!, region!!, object : Callback<Summoner> {
            override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                handleResponse(response, region)
            }

            override fun onFailure(call: Call<Summoner>, t: Throwable) {
                Log.e(TAG, "Error %s loading summoner".format(t.message))
                onSummonerLoadError(R.string.error404)
            }
        })
    }

    private fun loadLeagues(summonerRiotId: Long, region: String?, name: String?) {
        Log.i(TAG, "Loading leagues")
        SUMMONER_REPOSITORY.getSummonerLeagues(object : Callback<MutableMap<QueueType, QueueStats>> {

            override fun onResponse(call: Call<MutableMap<QueueType, QueueStats>>?,
                                    response: Response<MutableMap<QueueType, QueueStats>>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "Leagues loaded for summoner %s".format(summonerRiotId))
                    return onLoadSuccess(response.body())
                }
                if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // If summoner has no queue entries
                    Log.i(TAG, "No leagues found for summoner %s".format(summonerRiotId))
                    return onLoadSuccess(HashMap<QueueType, QueueStats>())
                }
                Log.e(TAG, "Error %s loading summoner leagues".format(response.message()))
                onLoadError(response.message())
            }

            override fun onFailure(call: Call<MutableMap<QueueType, QueueStats>>?, t: Throwable) {
                Log.e(TAG, "Error %s loading summoner details".format(t.message))
                onLoadError(R.string.error404)
            }

        }, region!!, summonerRiotId)
    }

    /**
     * Prepare required data to show in summoner stats.
     * Choose when a summoner update is required
     * Load summoner leagues
     * @param [summonerId] id in local database
     * @param [summonerRiotId] id for riot
     * @param [region] summoner region
     * @param [name] summoner name
     * @param [searchRequired] true if a summoner refresh from server is required
     */
    fun prepareSummonerStats(summonerId: Int, summonerRiotId: Long, region: String?, name: String?, searchRequired: Boolean) {
        if (mSummoner == null || mSummonerId != summonerId) {
            mSummonerId = summonerId
            mSummonerRiotId = summonerRiotId
            if (searchRequired) { // Should only happen in first load
                loadSummoner(summonerRiotId, region, name)
            } else {
                Log.i(TAG, "Loading Summoner %s from database".format(summonerRiotId))
                onSummonerFound(SUMMONER_REPOSITORY.getSummoner(summonerId)!!, true)
            }
        }
        if (mLeagues == null || mSummonerId != summonerId) {
            loadLeagues(summonerRiotId, region, name)
        }
    }

    //region Callbacks
    override fun onSummonerFound(summoner: Summoner, isLocal: Boolean) {
        mSummoner = summoner
        if (leaguesLoaded) {
            view.notifyDataIsReady(mSummoner!!, mLeagues)
        }
    }

    override fun onSummonerLoadError(message: String) {

    }

    override fun onSummonerLoadError(resId: Int) {

    }

    override fun onLoadProgressChange(p0: String?) {

    }

    override fun onLoadProgressChange(p0: Int) {

    }

    override fun onLoadSuccess(leagues: MutableMap<QueueType, QueueStats>?) {
        mLeagues = leagues
        leaguesLoaded = true
        if (mSummoner != null) {
            view.notifyDataIsReady(mSummoner!!, mLeagues)
        }
    }

    override fun onLoadError(p0: String?) {
        view.notifyError(p0)
    }

    override fun onLoadError(p0: Int) {

    }
    //endregion

}
