package com.andiag.welegends.views

import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface IPresenterSummonerLoader {
    fun onSummonerFound(summoner: Summoner, isLocal: Boolean)
    fun onSummonerLoadError(message: String)
    fun onSummonerLoadError(resId: Int)
}