package com.andiag.welegends.models

import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 10/02/2017.
 */
interface ISummonerRepository {

    fun read(id: Int, mode: LoadingType, callback: CallbackData<Summoner>)

    fun read(name: String, region: String, mode: LoadingType, callback: CallbackData<Summoner>)

    fun getSummonerLeagues(id: Long, region: String, callback: CallbackData<MutableMap<QueueType, QueueStats>>)

    fun getSummonerHistoric(limit: Int, callback: CallbackData<List<Summoner>>)

}