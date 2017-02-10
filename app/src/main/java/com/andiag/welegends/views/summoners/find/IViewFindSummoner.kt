package com.andiag.welegends.views.summoners.find

import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IViewFindSummoner {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerNotFound(message: String)
    fun onVersionLoaded(version: String)
}