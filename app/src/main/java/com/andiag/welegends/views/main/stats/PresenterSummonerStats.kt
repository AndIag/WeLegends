package com.andiag.welegends.views.main.stats

import android.util.Log
import com.andiag.commons.interfaces.presenters.AIErrorHandlerPresenter
import com.andiag.commons.interfaces.presenters.AISuccessHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.ISummonerRepository
import com.andiag.welegends.models.LoadingType
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.main.ActivityMain

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterSummonerStats(summonersRepository: ISummonerRepository)
    : AIPresenter<ActivityMain, FragmentSummonerStats>(),
        AISuccessHandlerPresenter<MutableMap<QueueType, QueueStats>>, AIErrorHandlerPresenter {

    private val TAG: String = PresenterSummonerStats::class.java.simpleName
    private val mSummonerRepository: ISummonerRepository = summonersRepository

    private var mSummonerId: Int? = null
    private var mSummonerRiotId: Long? = null

    private var mSummoner: Summoner? = null
    private var mLeagues: MutableMap<QueueType, QueueStats>? = null
    private var leaguesLoaded: Boolean = false

    constructor() : this(SummonerRepository.getInstance())

    private fun loadLeagues(summonerRiotId: Long, region: String?, name: String?) {
        Log.i(TAG, "Loading leagues")
        mSummonerRepository.getSummonerLeagues(summonerRiotId, region!!, object : CallbackData<MutableMap<QueueType, QueueStats>> {

            override fun onSuccess(data: MutableMap<QueueType, QueueStats>?) {
                this@PresenterSummonerStats.onSuccess(data!!)
            }

            override fun onError(t: Throwable?) {
                this@PresenterSummonerStats.onError(t!!.message)
            }

        })
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
                Log.i(TAG, "Loading Summoner %s from server".format(summonerRiotId))
                mSummonerRepository.read(name!!, region!!, LoadingType.REMOTE_LOAD, object : CallbackData<Summoner> {
                    override fun onSuccess(data: Summoner?) {
                        onSummonerFound(data!!)
                    }

                    override fun onError(t: Throwable?) {
                        Log.e(TAG, "Error %s loading summoner".format(t!!.message))
                        onSummonerLoadError(t.message!!)
                    }

                })
            } else {
                Log.i(TAG, "Loading Summoner %s from database".format(summonerRiotId))
                mSummonerRepository.read(name!!, region!!, LoadingType.LOCAL_LOAD, object : CallbackData<Summoner> {
                    override fun onSuccess(data: Summoner?) {
                        onSummonerFound(data!!)
                    }

                    override fun onError(t: Throwable?) {
                        Log.e(TAG, "Error %s loading summoner".format(t!!.message))
                        onSummonerLoadError(t.message!!)
                    }

                })
            }
        }
        if (mLeagues == null || mSummonerId != summonerId) {
            loadLeagues(summonerRiotId, region, name)
        }
    }

    //region Callbacks
    fun onSummonerFound(summoner: Summoner) {
        mSummoner = summoner
        if (leaguesLoaded) {
            view.notifyDataIsReady(mSummoner!!, mLeagues)
        }
    }

    fun onSummonerLoadError(message: String) {

    }

    override fun onSuccess(leagues: MutableMap<QueueType, QueueStats>) {
        mLeagues = leagues
        leaguesLoaded = true
        if (mSummoner != null) {
            view.notifyDataIsReady(mSummoner!!, mLeagues)
        }
    }

    override fun onError(p0: String?) {
        view.notifyError(p0)
    }

    override fun onError(p0: Int) {

    }
    //endregion

}
