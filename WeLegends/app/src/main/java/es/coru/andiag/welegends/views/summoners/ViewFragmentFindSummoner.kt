package es.coru.andiag.welegends.views.summoners

import es.coru.andiag.andiag_mvp.views.AIInterfaceFragmentView
import es.coru.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentFindSummoner : AIInterfaceFragmentView {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerNotFound(error: Int)
    fun onVersionUpdate(version: String)
}