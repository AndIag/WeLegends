package com.andiag.welegends.models

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.presenters.commons.PresenterSummonerLoader

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
     * Try to find a [Summoner] in local database or in riot server
     * @param [caller] handles request responses
     * @param [name]
     * @param [region]
     * @return [PresenterSummonerLoader.onSummonerFound] with [SummonerRepository] data
     *      or call [PresenterSummonerLoader.onSummonerLoadError]
     */
    fun getSummoner(caller: PresenterSummonerLoader, name: String, region: String)

    /**
     * Request a [Summoner] from riot database
     * @param [caller] handles request responses
     * @param [name]
     * @param [region]
     * @return [Summoner] or null
     */
    fun loadSummoner(caller: PresenterSummonerLoader, name: String, region: String)

    /**
     * Request summoner leagues
     * @param [caller] handles request responses
     * @param [region] summoner server region
     * @param [id] summoner id
     * @return [AIInterfaceLoaderHandlerPresenter.onLoadSuccess] with leagues in [Map] format
     *      or call [AIInterfaceLoaderHandlerPresenter.onLoadError]
     */
    fun getSummonerLeagues(caller: AIInterfaceLoaderHandlerPresenter<MutableMap<QueueType, QueueStats>>,
                           region: String, id: Long)

    /**
     * Return a list of summoners order by date DESC
     * @param [caller] contains error and success callbacks
     * @param [limit] max number of elements returned
     * @return [MutableList] of [SummonerRepository]
     */
    fun getSummonerHistoric(caller: AIInterfaceLoaderHandlerPresenter<List<Summoner>>, limit: Int)
}