package es.coru.andiag.welegends.views.summoners

import es.coru.andiag.andiag_mvp.interfaces.BaseFragmentView
import es.coru.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by andyq on 09/12/2016.
 */
interface ViewFragmentSummonerList : BaseFragmentView {
    fun onSummonersLoaded(summoners: List<Summoner>)
    fun onSummonersEmpty(error: Int?)
}