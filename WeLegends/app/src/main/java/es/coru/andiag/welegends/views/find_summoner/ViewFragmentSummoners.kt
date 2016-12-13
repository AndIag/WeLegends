package es.coru.andiag.welegends.views.find_summoner

import es.coru.andiag.andiag_mvp.base.BaseFragmentView
import es.coru.andiag.welegends.models.entities.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentSummoners : BaseFragmentView {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: Int?)
}