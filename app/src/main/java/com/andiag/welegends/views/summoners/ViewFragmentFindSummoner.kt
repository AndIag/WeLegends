package com.andiag.welegends.views.summoners

import com.andiag.welegends.models.entities.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentFindSummoner {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerNotFound(error: Int)
    fun onSummonerNotFound(message: String)
    fun onVersionLoaded(version: String)
    fun onVersionLoadError(error: Int)
    fun onVersionLoadError(error: String)
    fun onStaticDataLoadChange(message: String?)
}