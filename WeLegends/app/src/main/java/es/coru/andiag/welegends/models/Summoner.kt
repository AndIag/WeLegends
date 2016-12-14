package es.coru.andiag.welegends.models

import android.util.Log
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.utils.StringUtils
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner_Table
import es.coru.andiag.welegends.presenters.summoners.PresenterSummonerLoader
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Canalejas on 14/12/2016.
 */
object Summoner {
    private val TAG: String = Summoner::class.java.simpleName

    fun getSummonerByName(caller: PresenterSummonerLoader, name: String, region: String) {
//        val cleanName = URLEncoder.encode(StringUtils.cleanString(name), "UTF-8")//TODO get format from mobile
        val cleanName = StringUtils.cleanString(name)
        if (!cleanName.isEmpty()) {
            Log.i(TAG, "Searching summoner %s in database".format(cleanName))
            var summoner: Summoner? = SQLite.select().from<Summoner>(Summoner::class.java)
                    .where(Summoner_Table.name.eq(name))
                    .and(Summoner_Table.region.eq(region))
                    .querySingle()
            if (summoner == null) {
                val call = RestClient.getWeLegendsData(cleanName).getSummonerByName(region, cleanName)
                call.enqueue(object : Callback<Summoner> {
                    override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                        if (response.isSuccessful) {
                            doAsync {
                                Log.d(TAG, response.body().toString())
                                summoner = response.body()
                                summoner!!.region = region
                                summoner!!.lastUpdate = Calendar.getInstance().timeInMillis
                                Log.i(TAG, "Saving new summoner %s".format(summoner!!.name))
                                summoner!!.save()
                                uiThread {
                                    caller.onSummonerFound(summoner!!)
                                }
                            }
                            return
                        }
                        Log.e(TAG, response.message())
                        caller.onSummonerLoadError(response.message())
                    }

                    override fun onFailure(call: Call<Summoner>, t: Throwable) {
                        Log.e(TAG, t.message)
                        caller.onSummonerLoadError(R.string.error404)
                    }
                })
            } else {
                Log.i(TAG, "Summoner %s found in database".format(summoner!!.name))
                caller.onSummonerFound(summoner!!)
            }
        } else {
            Log.e(TAG, "Empty summoner")
            caller.onSummonerLoadError(R.string.voidSummonerError)
        }
    }

}