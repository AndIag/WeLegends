package es.coru.andiag.welegends.presenters.summoners

import android.content.Context
import android.util.Log
import com.raizlabs.android.dbflow.sql.language.SQLite
import es.coru.andiag.andiag_mvp.base.BaseFragmentPresenter
import es.coru.andiag.andiag_mvp.interfaces.DataLoaderPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.utils.StringUtils
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.api.RestClient
import es.coru.andiag.welegends.models.database.Summoner
import es.coru.andiag.welegends.models.database.Summoner_Table
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentFindSummoner
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : BaseFragmentPresenter<FragmentFindSummoner, ActivitySummoners>(), DataLoaderPresenter<String> {

    override fun onViewAttached() {
        Version.checkServerVersion(this)
    }

    override fun onViewDetached() {

    }

    //region FindSummoner
    fun getSummonerByName(name: String, region: String) {
        Log.d(TAG, "searchSummonerByName")
        val cleanName = StringUtils.cleanString(name)
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
                                    view.onSummonerFound(dbSummoner!!)
                                }
                            }
                        }
                        // TODO handle not found summoners
                    }

                    override fun onFailure(call: Call<Summoner>, t: Throwable) {
                        view!!.onSummonerNotFound(R.string.error404)
                    }
                })
            } else {
                view!!.onSummonerFound(dbSummoner!!)
            }
        } else {
            view!!.onSummonerNotFound(R.string.voidSummonerError)
        }
    }
    //endregion

    override fun getContext(): Context {
        return parent!!
    }

    override fun onLoadSuccess(message: String?, data: String?) {
        view!!.onVersionUpdate(data!!)
        parent!!.showLoading()
    }

    override fun onLoadProgressChange(message: String?, data: String?) {
        view!!.onVersionUpdate(message!!)
    }

    override fun onLoadError(message: String?) {
        parent!!.hideLoading()
        //TODO give some type of feedback and close app
    }

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }

}