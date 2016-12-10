package es.coru.andiag.welegends.find

import es.coru.andiag.welegends.common.base.BaseLoadingView
import es.coru.andiag.welegends.models.entities.database.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentFindSummoner : BaseLoadingView {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerNotFound(error: Int)
}