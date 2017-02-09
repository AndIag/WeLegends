package com.andiag.welegends.presenters.commons

import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface PresenterSummonerLoader {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: String)
    fun onSummonerLoadError(resId: Int)
}