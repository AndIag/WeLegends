package es.coru.andiag.welegends.find

import es.coru.andiag.welegends.common.BaseView
import es.coru.andiag.welegends.models.entities.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentSummoners : BaseView {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: Int?)
}