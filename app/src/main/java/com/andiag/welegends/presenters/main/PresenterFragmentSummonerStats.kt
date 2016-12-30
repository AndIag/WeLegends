package com.andiag.welegends.presenters.main

import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.views.main.ActivityMain
import com.andiag.welegends.views.main.FragmentSummonerStats

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterFragmentSummonerStats private constructor() : AIPresenter<ActivityMain, FragmentSummonerStats>() {
    private val TAG: String = PresenterFragmentSummonerStats::class.java.simpleName

    private var mSummonerId: Long? = null
    private var mSummonerRiotId: Long? = null
    private var mRegion: String? = null
    private var mLevel: Int? = null
    private var mSearchRequired: Boolean? = null

    fun prepareSummonerStats(summonerId: Long?, summonerRiotId: Long?, region: String?, level: Int?, searchRequired: Boolean?) {
        // TODO
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
