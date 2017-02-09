package com.andiag.welegends.presenters.main

import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.presenters.commons.PresenterSummonerLoader
import com.andiag.welegends.views.main.ActivityMain
import com.andiag.welegends.views.main.FragmentSummonerStats

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterFragmentSummonerStats : AIPresenter<ActivityMain, FragmentSummonerStats>(), PresenterSummonerLoader, AIInterfaceLoaderHandlerPresenter<MutableMap<QueueType, QueueStats>> {
    private val TAG: String = PresenterFragmentSummonerStats::class.java.simpleName

    private var mSummonerId: Int? = null
    private var mSummonerRiotId: Long? = null

    private var mSummoner: Summoner? = null
    private var mLeagues: MutableMap<QueueType, QueueStats>? = null
    private var leaguesLoaded: Boolean = false

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
                SummonerRepository.getRiotSummonerByName(this, name!!, region!!)
            } else {
                Log.i(TAG, "Loading Summoner %s from database".format(summonerRiotId))
                onSummonerFound(SummonerRepository.getLocalSummonerById(summonerId)!!)
            }
        }
        if (mLeagues == null || mSummonerId != summonerId) {
            Log.i(TAG, "Loading leagues")
            SummonerRepository.getSummonerLeagues(this, region!!, summonerRiotId)
        }
    }

    //region Callbacks
    override fun onSummonerFound(summoner: Summoner) {
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

    companion object {
        private var presenter: PresenterFragmentSummonerStats? = null

        fun getInstance(): PresenterFragmentSummonerStats {
            if (presenter == null) {
                presenter = PresenterFragmentSummonerStats()
            }
            return presenter!!
        }
    }

}
