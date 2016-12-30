package com.andiag.welegends.views.summoners

import com.andiag.welegends.models.entities.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentSummonerList {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: Int?)
}