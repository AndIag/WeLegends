package com.andiag.welegends.views.main

import android.os.Bundle
import android.view.View
import com.andiag.commons.fragments.AIButterFragment
import com.andiag.welegends.R
import com.andiag.welegends.presenters.main.PresenterFragmentSummonerStats

/**
 * Created by Canalejas on 30/12/2016.
 */

class FragmentSummonerStats : AIButterFragment<PresenterFragmentSummonerStats>() {

    companion object {
        val CONF_SEARCH_REQUIRED: String = ActivityMain.CONF_SEARCH_REQUIRED
        val VAL_SUMMONER_ID: String = ActivityMain.VAL_SUMMONER_ID
        val VAL_SUMMONER_RIOT_ID: String = ActivityMain.VAL_SUMMONER_RIOT_ID
        val VAL_SUMMONER_LVL: String = ActivityMain.VAL_SUMMONER_LVL
        val VAL_REGION: String = ActivityMain.VAL_REGION
    }

    private var mSummonerId: Long? = null
    private var mSummonerRiotId: Long? = null

    override fun onInitLayout() {
        mFragmentLayout = R.layout.fragment_summoner_stats
    }

    override fun onInitPresenter() {
        mPresenter = PresenterFragmentSummonerStats.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putLong(VAL_SUMMONER_ID, mSummonerId!!)
        outState.putLong(VAL_SUMMONER_RIOT_ID, mSummonerRiotId!!)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare Summoner data to show
        prepareSummonerStats(arguments, savedInstanceState)
    }

    /**
     * Parse Summoner data and set it in presenter for load
     */
    private fun prepareSummonerStats(args: Bundle?, savedInstanceState: Bundle?) {
        var region: String? = null
        var level: Int? = null
        var searchRequired: Boolean? = null

        if (args != null) {
            mSummonerId = args.getLong(VAL_SUMMONER_ID)
            mSummonerRiotId = args.getLong(VAL_SUMMONER_RIOT_ID)
            region = args.getString(VAL_REGION)
            level = args.getInt(VAL_SUMMONER_LVL)
            searchRequired = args.getBoolean(CONF_SEARCH_REQUIRED)
        }

        if (savedInstanceState != null) {
            mSummonerId = savedInstanceState.getLong(VAL_SUMMONER_ID)
            mSummonerRiotId = savedInstanceState.getLong(VAL_SUMMONER_RIOT_ID)
            searchRequired = false
        }

        mPresenter.prepareSummonerStats(mSummonerId, mSummonerRiotId, region, level, searchRequired)
    }

}
