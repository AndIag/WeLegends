package es.coru.andiag.welegends.presenters.summoners

import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andoiag.andiag_mvp_utils.interfaces.AIInterfaceLoaderHandlerPresenter

/**
 * Created by Canalejas on 14/12/2016.
 */
interface PresenterSummonerLoader : AIInterfaceLoaderHandlerPresenter<String> {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: String)
    fun onSummonerLoadError(resId: Int)
}