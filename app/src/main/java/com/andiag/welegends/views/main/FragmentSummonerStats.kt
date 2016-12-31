package com.andiag.welegends.views.main

import android.os.Bundle
import android.view.View
import com.andiag.commons.fragments.AIButterFragment
import com.andiag.welegends.R
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.models.entities.league.QueueStats
import com.andiag.welegends.models.entities.league.QueueType
import com.andiag.welegends.presenters.main.PresenterFragmentSummonerStats
import org.jetbrains.anko.toast

/**
 * Created by Canalejas on 30/12/2016.
 */

class FragmentSummonerStats : AIButterFragment<PresenterFragmentSummonerStats>() {

    companion object {
        val TAG: String = FragmentSummonerStats::class.java.simpleName
        val CONF_SEARCH_REQUIRED: String = ActivityMain.CONF_SEARCH_REQUIRED
        val VAL_SUMMONER_ID: String = ActivityMain.VAL_SUMMONER_ID
        val VAL_SUMMONER_RIOT_ID: String = ActivityMain.VAL_SUMMONER_RIOT_ID
        val VAL_SUMMONER_NAME: String = ActivityMain.VAL_SUMMONER_NAME
        val VAL_SUMMONER_REGION: String = ActivityMain.VAL_SUMMONER_REGION
    }

    private var mSummonerId: Int? = null
    private var mSummonerRiotId: Long? = null
    private var mRegion: String? = null

    override fun onInitLayout() {
        mFragmentLayout = R.layout.fragment_summoner_stats
    }

    override fun onInitPresenter() {
        mPresenter = PresenterFragmentSummonerStats.getInstance()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(VAL_SUMMONER_ID, mSummonerId!!)
        outState.putLong(VAL_SUMMONER_RIOT_ID, mSummonerRiotId!!)
        outState.putString(VAL_SUMMONER_REGION, mRegion!!)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare Summoner data to show
        prepareSummonerStats(arguments, savedInstanceState)
    }

    fun notifyDataIsReady(summoner: Summoner, leagues: MutableMap<QueueType, QueueStats>?) {
        mParentContext.toast("Data Load Ends")
        // TODO
    }

    /**
     * Parse Summoner data and set it in presenter for load
     */
    private fun prepareSummonerStats(args: Bundle?, savedInstanceState: Bundle?) { // TODO review this
        var name: String? = null
        var searchRequired: Boolean? = null

        /**
         * This means fragment was just launched from the activity so we might need to
         * refresh our summoner info (searchRequired = true) so we need to pass all this data
         * to the presenter
         */
        if (args != null) {
            mSummonerId = args.getInt(VAL_SUMMONER_ID)
            mSummonerRiotId = args.getLong(VAL_SUMMONER_RIOT_ID)
            mRegion = args.getString(VAL_SUMMONER_REGION)
            name = args.getString(VAL_SUMMONER_NAME)
            searchRequired = args.getBoolean(CONF_SEARCH_REQUIRED)
        }

        /**
         * In this case we need less data and we can forget about refresh the summoner
         */
        if (savedInstanceState != null) {
            mSummonerId = savedInstanceState.getInt(VAL_SUMMONER_ID)
            mSummonerRiotId = savedInstanceState.getLong(VAL_SUMMONER_RIOT_ID)
            mRegion = savedInstanceState.getString(VAL_SUMMONER_REGION)
            searchRequired = false
        }

        mPresenter.prepareSummonerStats(mSummonerId!!, mSummonerRiotId!!, mRegion, name, searchRequired!!)
    }

}
