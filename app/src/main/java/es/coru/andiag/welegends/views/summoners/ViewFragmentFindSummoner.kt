package es.coru.andiag.welegends.views.summoners

import es.coru.andiag.welegends.models.wrapped.database.Summoner

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