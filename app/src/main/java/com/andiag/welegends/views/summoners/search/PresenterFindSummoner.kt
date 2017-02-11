package com.andiag.welegends.views.summoners.search

import android.support.design.widget.Snackbar
import android.util.Log
import com.andiag.commons.interfaces.presenters.AIInterfaceErrorHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.WeLegendsDatabase
import com.andiag.welegends.common.base.ActivityBase
import com.andiag.welegends.models.ISummonerRepository
import com.andiag.welegends.models.IVersionRepository
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.models.database.static_data.*
import com.andiag.welegends.models.utils.CallbackData
import com.andiag.welegends.models.utils.CallbackSemaphore
import com.andiag.welegends.views.summoners.ActivitySummoners
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Callable


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFindSummoner(summonersRepository: ISummonerRepository, versionRepository: IVersionRepository)
    : AIPresenter<ActivitySummoners, FragmentFindSummoner>(), AIInterfaceErrorHandlerPresenter {

    private val TAG = PresenterFindSummoner::class.java.simpleName
    private val VERSION_REPOSITORY: IVersionRepository = versionRepository
    private val SUMMONER_REPOSITORY: ISummonerRepository = summonersRepository

    private val VERSION_CALLBACK: Callback<List<String>> = object : Callback<List<String>> {
        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
            if (response.isSuccessful) {
                doAsync {
                    val newVersion: String = response.body()[0]
                    Log.i(TAG, "Server Version: %s".format(newVersion))
                    if (newVersion != VERSION_REPOSITORY.getSavedVersion(context)) {


                        WeLegendsDatabase.recreateStaticTables(context)

                        uiThread {
                            loadStaticData(newVersion)
                        }
                    }
                    uiThread {
                        VERSION_REPOSITORY.setVersion(newVersion, context) //Comment this line to test static data load
                        if (isViewCreated) {
                            view!!.onVersionLoaded(newVersion)
                        }
                        Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
                    }
                }
                return
            }
            onLoadError(response.message())
        }

        override fun onFailure(call: Call<List<String>>, t: Throwable) {
            Log.e(TAG, "ERROR: checkServerVersion - onFailure: %s".format(t.message))
            onLoadError(t.message)
        }
    }

    var version: String? = null

    constructor() : this(SummonerRepository.getInstance(), VersionRepository.getInstance())

    override fun onViewAttached() {
        this.version = VERSION_REPOSITORY.getVersion()
    }

    //region Version
    private fun loadStaticData(newVersion: String) {
        val locale = Locale.getDefault().toString()

        Log.i(TAG, "Updated Server Version To: %s".format(newVersion))
        Log.i(TAG, "Mobile Locale: %s".format(locale))

        //Init semaphore with number of methods to load and callback method
        val semaphore: CallbackSemaphore = CallbackSemaphore(5, Callable {
            Log.i(TAG, "CallbackSemaphore: StaticData Load Ended")
        })
        semaphore.acquire(5)

        //Load static data. !IMPORTANT change semaphore if some method change
        Champion.loadFromServer(this, semaphore, newVersion, locale)
        Item.loadFromServer(this, semaphore, newVersion, locale)
        SummonerSpell.loadFromServer(this, semaphore, newVersion, locale)
        Mastery.loadFromServer(this, semaphore, newVersion, locale)
        Rune.loadFromServer(this, semaphore, newVersion, locale)
    }

    /**
     * Return server version or null if still loading
     */
    fun getServerVersion(): String? {
        version = VERSION_REPOSITORY.getVersion()
        if (version == null) {
            VERSION_REPOSITORY.loadVersion(VERSION_CALLBACK)
        }
        return version
    }

    fun isLoadingVersion(): Boolean {
        return VERSION_REPOSITORY.isLoading()
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadError(message: String?) {
        Log.e(TAG, message)
        Snackbar.make(view.view!!, R.string.error_loading_static_data, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, { VERSION_REPOSITORY.loadVersion(VERSION_CALLBACK) })
                .setActionTextColor(ActivityBase.resolveColorAttribute(context, R.attr.mainColor))
                .show()
    }

    override fun onLoadError(resId: Int) {
        Log.e(TAG, context.getString(resId))
        Snackbar.make(view.view!!, R.string.error_loading_static_data, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, { VERSION_REPOSITORY.loadVersion(VERSION_CALLBACK) })
                .setActionTextColor(ActivityBase.resolveColorAttribute(context, R.attr.mainColor))
                .show()
    }
    //endregion
    //endregion

    //region Summoner

    fun getSummonerByName(name: String, region: String) {
        if (!name.isEmpty()) {
            // Try to find summoner
            SUMMONER_REPOSITORY.getSummoner(name,region,object : CallbackData<Summoner>{
                override fun onSuccess(data: Summoner) {
                    onSummonerFound(data,true)
                }

                override fun onError(t: Throwable?) {
                    onSummonerLoadError(R.string.error404)
                }
            })
        } else {
            Log.e(TAG, "Empty summoner")
            onSummonerLoadError(R.string.voidSummonerError)
        }
    }

    fun onSummonerFound(summoner: Summoner, isLocal: Boolean) {
        if (isViewCreated) {
            view!!.onSummonerFound(summoner, isLocal)
        }
    }

    fun onSummonerLoadError(resId: Int) {
        if (isViewCreated) {
            view!!.onSummonerNotFound(view!!.getString(resId))
        }
    }

    fun onSummonerLoadError(message: String) {
        if (isViewCreated) {
            view!!.onSummonerNotFound(message)
        }
    }
    //endregion

}