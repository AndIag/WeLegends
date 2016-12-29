package com.andiag.welegends.presenters.summoners

import com.andiag.libraryutils.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.models.wrapped.database.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface PresenterSummonerLoader : AIInterfaceLoaderHandlerPresenter<String> {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: String)
    fun onSummonerLoadError(resId: Int)
}