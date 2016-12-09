package es.coru.andiag.welegends.find

import android.util.Log
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.BasePresenter
import es.coru.andiag.welegends.common.Utils
import es.coru.andiag.welegends.models.entities.database.Summoner
import es.coru.andiag.welegends.models.rest.RestClient
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : BasePresenter<ViewFragmentFindSummoner>() {

    fun getSummonerByName(name: String, region: String) {
        Log.d(TAG, "getSummonerByName $name region $region")
        searchSummonerByName(name, region)
    }

    private fun onSuccess(data: Summoner) {
        getView().onSummonerFound(data)
    }

    private fun onError(errorMessage: Int) {
        getView().onSummonerNotFound(errorMessage)
    }

    private fun searchSummonerInApi(summonerName: String, region: String, callback: Callback<Summoner>) {
        val call = RestClient.getWeLegendsData(summonerName).getSummonerByName(region, summonerName)
        call.enqueue(callback)
    }

    fun searchSummonerByName(name: String, region: String) {
        Log.d(TAG, "searchSummonerByName")
        doAsync {
            val cleanName = Utils.cleanString(name)
            if (!cleanName.isEmpty()) {
                val summoner: Summoner? = null//db.getSummonerByName(cleanName, region)
                if (summoner == null) {
                    searchSummonerInApi(cleanName, region, object : Callback<Summoner> {
                        override fun onResponse(call: Call<Summoner>, response: Response<Summoner>) {
                            var s : Summoner = response.body()
                            s.region = region
                            //db.addSummoner(summoner)
                            uiThread { onSuccess(s) }
                        }

                        override fun onFailure(call: Call<Summoner>, t: Throwable) {
                            uiThread { onError(R.string.error404) }
                        }
                    })
                } else {
                    uiThread { onSuccess(summoner) }
                }
            } else {
                uiThread { onError(R.string.voidSummonerError) }
            }
        }
    }

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }
}