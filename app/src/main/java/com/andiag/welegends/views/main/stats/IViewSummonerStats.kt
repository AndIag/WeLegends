package com.andiag.welegends.views.main.stats

import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.database.Summoner

/**
 * Created by Canalejas on 10/02/2017.
 */
interface IViewSummonerStats {
    fun notifyError(error: String?)
    fun notifyDataIsReady(summoner: Summoner, leagues: MutableMap<QueueType, QueueStats>?)
}