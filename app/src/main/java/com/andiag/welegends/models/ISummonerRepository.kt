package com.andiag.welegends.models

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.database.Summoner
import retrofit2.Callback

/**
 * Created by Canalejas on 10/02/2017.
 */
interface ISummonerRepository {
    /**
     * Try to find a summoner in local database
     * @param [id] summoner id
     * @return [Summoner] or null
     */
    fun getSummoner(id: Int): Summoner?

    /**
     * Try to find a [Summoner] in local database
     * @param [name]
     * @param [region]
     * @return [Summoner] or null
     */
    fun getSummoner(name: String, region: String): Summoner?

    /**
     * Request a [Summoner] from riot database
     * @param [name]
     * @param [region]
     * @param [callback]
     * @return [Summoner] or null
     */
    fun loadSummoner(name: String, region: String, callback: Callback<Summoner>)

    /**
     * Request a [Summoner] from local or riot database.
     * @param [name]
     * @param [region]
     * @param [callback]
     * @return [Summoner] or null
     */
    fun getSummoner(name:String, region:String, callback: CallbackData<Summoner>)

    /**
     * Request summoner leagues
     * @param [callback] handles request responses
     * @param [region] summoner server region
     * @param [id] summoner id
     * @return [AIInterfaceLoaderHandlerPresenter.onLoadSuccess] with leagues in [Map] format
     *      or call [AIInterfaceLoaderHandlerPresenter.onLoadError]
     */
    fun getSummonerLeagues(callback: Callback<MutableMap<QueueType, QueueStats>>, region: String, id: Long)


    fun getSummonerHistoric(limit: Int, callback: CallbackData<List<Summoner>?>)
}