package es.coru.andiag.welegends.views.summoners

import es.coru.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentSummonerList {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: Int?)
}