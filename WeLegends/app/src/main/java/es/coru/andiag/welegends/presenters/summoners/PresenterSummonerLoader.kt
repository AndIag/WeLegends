package es.coru.andiag.welegends.presenters.summoners

import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface PresenterSummonerLoader : AIInterfaceLoaderPresenter<String> {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: Int)
}