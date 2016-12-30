package com.andiag.welegends.presenters.main

import com.andiag.commons.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.models.EPSummoner
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.models.entities.league.QueueStats
import com.andiag.welegends.models.entities.league.QueueType
import com.andiag.welegends.presenters.commons.PresenterSummonerLoader
import com.andiag.welegends.views.main.ActivityMain
import com.andiag.welegends.views.main.FragmentSummonerStats

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterFragmentSummonerStats private constructor() : AIPresenter<ActivityMain, FragmentSummonerStats>(), PresenterSummonerLoader, AIInterfaceLoaderHandlerPresenter<Map<QueueType, QueueStats>> {
    private val TAG: String = PresenterFragmentSummonerStats::class.java.simpleName

    private var mSummonerId: Int? = null
    private var mSummonerRiotId: Long? = null

    private var mSummoner: Summoner? = null
    private var mLeagues: Map<QueueType, QueueStats>? = null

    fun prepareSummonerStats(summonerId: Int, summonerRiotId: Long, region: String?, name: String?, searchRequired: Boolean) {
        if (searchRequired) { // Should only happen in first load
            mSummonerId = summonerId
            mSummonerRiotId = summonerRiotId
            EPSummoner.getRiotSummonerByName(this, name!!, region!!)
        } else {
            onSummonerFound(EPSummoner.getLocalSummonerById(summonerId)!!)
        }
        EPSummoner.getSummonerDetails(this, region!!, summonerRiotId)
    }

    override fun onSummonerFound(summoner: Summoner) {
        mSummoner = summoner
    }

    override fun onSummonerLoadError(message: String) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSummonerLoadError(resId: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadProgressChange(p0: String?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadProgressChange(p0: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadSuccess(leagues: Map<QueueType, QueueStats>?) {
        mLeagues = leagues
    }

    override fun onLoadError(p0: String?) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadError(p0: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
