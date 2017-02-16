package com.andiag.welegends.views.summoners.history

import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IViewSummonerList {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: String?)
}