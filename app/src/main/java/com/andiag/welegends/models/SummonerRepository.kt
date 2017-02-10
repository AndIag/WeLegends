package com.andiag.welegends.models

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.welegends.common.entities.league.QueueStats
import com.andiag.welegends.common.entities.league.QueueType
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.models.database.Summoner_Table
import com.raizlabs.android.dbflow.annotation.Collate
import com.raizlabs.android.dbflow.sql.language.SQLite
import retrofit2.Callback
import java.net.URLEncoder

/**
 * Created by Canalejas on 14/12/2016.
 */
class SummonerRepository private constructor() : ISummonerRepository {
    private val TAG: String = Summoner::class.java.simpleName

    companion object {
        private var REPOSITORY: ISummonerRepository? = null

        fun getInstance(): ISummonerRepository {
            if (REPOSITORY == null) {
                REPOSITORY = SummonerRepository()
            }
            return REPOSITORY!!
        }

    }

    override fun getSummoner(id: Int): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.mid.eq(id))
                .querySingle()
    }

    override fun getSummoner(name: String, region: String): Summoner? {
        return SQLite.select().from<Summoner>(Summoner::class.java)
                .where(Summoner_Table.name.eq(name).collate(Collate.NOCASE))
                .and(Summoner_Table.region.eq(region))
                .querySingle()
    }

    override fun loadSummoner(name: String, region: String, callback: Callback<Summoner>) {
        val call = RestClient.getWeLegendsData().getSummonerByName(region, URLEncoder.encode(name, "UTF-8"))
        call.enqueue(callback)
    }

    override fun getSummonerLeagues(callback: Callback<MutableMap<QueueType, QueueStats>>, region: String, id: Long) {
        val call = RestClient.getWeLegendsData().getSummonerDetails(id, region)
        call.enqueue(callback)
    }

    override fun getSummonerHistoric(caller: AIInterfaceLoaderHandlerPresenter<List<Summoner>>, limit: Int) {
        caller.onLoadSuccess(SQLite.select().from<Summoner>(Summoner::class.java)
                .orderBy(Summoner_Table.lastUpdate, false).limit(limit).queryList())
    }

}