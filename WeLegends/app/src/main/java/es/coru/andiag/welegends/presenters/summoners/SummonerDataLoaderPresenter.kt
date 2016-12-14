package es.coru.andiag.welegends.presenters.summoners

import es.coru.andiag.andiag_mvp.interfaces.DataLoaderPresenter
import es.coru.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface SummonerDataLoaderPresenter : DataLoaderPresenter<String> {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: Int)
}