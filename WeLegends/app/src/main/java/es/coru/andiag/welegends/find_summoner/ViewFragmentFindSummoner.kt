package es.coru.andiag.welegends.find_summoner

import es.coru.andiag.welegends.common.mvp.base.BaseFragmentView
import es.coru.andiag.welegends.models.entities.database.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentFindSummoner : BaseFragmentView {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerNotFound(error: Int)
    fun onVersionUpdate(version: String)
}