package com.andiag.welegends.presenters.summoners

import com.andiag.commons.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.models.entities.Summoner

/**
 * Created by Canalejas on 14/12/2016.
 */
interface PresenterSummonerLoader : AIInterfaceLoaderHandlerPresenter<String> {
    fun onSummonerFound(summoner: Summoner)
    fun onSummonerLoadError(message: String)
    fun onSummonerLoadError(resId: Int)
}