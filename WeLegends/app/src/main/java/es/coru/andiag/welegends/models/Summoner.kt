package es.coru.andiag.welegends.models

import android.util.Log
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.utils.StringUtils
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner_Table
import es.coru.andiag.welegends.presenters.summoners.SummonerDataLoaderPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.util.*

/**
 * Created by Canalejas on 14/12/2016.
 */
object Summoner {
    private val TAG: String = Summoner::class.java.simpleName

    fun getSummonerByName(caller: SummonerDataLoaderPresenter, name: String, region: String) {
        Log.d(TAG, "searchSummonerByName")
        val cleanName = URLEncoder.encode(StringUtils.cleanString(name), "UTF-8")//TODO get format from mobile
        if (!cleanName.isEmpty()) {
            var dbSummoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                    .where(Summoner_Table.name.eq(name))
                    .and(Summoner_Table.region.eq(region))
                    .querySingle()
            if (dbSummoner == null) {
                val call = RestClient.getWeLegendsData(cleanName).getSummonerByName(region, cleanName)
                call.enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                        if (response.isSuccessful) {
                            doAsync {
                                dbSummoner = response.body()
                                dbSummoner!!.region = region
                                dbSummoner!!.lastUpdate = Calendar.getInstance().timeInMillis
                                dbSummoner!!.save()
                                uiThread {
                                    caller.onSummonerFound(dbSummoner!!)
                                }
                            }
                        }
                        // TODO handle not found summoners
                    }

                    override fun onFailure(call: Call<Summoner>, t: Throwable) {
                        caller.onSummonerLoadError(R.string.error404)
                    }
                })
            } else {
                caller.onSummonerFound(dbSummoner!!)
            }
        } else {
            caller.onSummonerLoadError(R.string.voidSummonerError)
        }
    }

}