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

    override fun onInitLayout() {
        mFragmentLayout = R.layout.fragment_summoner_stats
    }

    override fun onInitPresenter() {
        mPresenter = PresenterFragmentSummonerStats.getInstance()
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {

        }

        if (savedInstanceState != null) {

        }

    }
}
