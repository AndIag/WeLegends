package com.andiag.welegends.presenters.main

import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.views.main.ActivityMain
import com.andiag.welegends.views.main.FragmentSummonerStats

/**
 * Created by Canalejas on 30/12/2016.
 */

class PresenterFragmentSummonerStats private constructor() : AIPresenter<ActivityMain, FragmentSummonerStats>() {
    // TODO not implemented jet


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
