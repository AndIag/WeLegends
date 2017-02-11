package com.andiag.welegends.views.summoners.history

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.ISummonerRepository
import com.andiag.welegends.models.IVersionRepository
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.models.utils.CallbackData
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.search.PresenterFindSummoner

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterSummonerList
(summonersRepository: ISummonerRepository, versionRepository: IVersionRepository)
    : AIPresenter<ActivitySummoners, FragmentSummonerList>(),
        AIInterfaceLoaderHandlerPresenter<List<Summoner>> {

    private val TAG = PresenterFindSummoner::class.java.simpleName
    private val VERSION_REPOSITORY: IVersionRepository = versionRepository
    private val SUMMONER_REPOSITORY: ISummonerRepository = summonersRepository

    var version: String? = null

    constructor() : this(SummonerRepository.getInstance(), VersionRepository.getInstance())

    override fun onViewAttached() {
    }

    fun loadSummoners() {
        SUMMONER_REPOSITORY.getSummonerHistoric(20,object : CallbackData<List<Summoner>?> {
            override fun onSuccess(data: List<Summoner>?) {
                onLoadSuccess(data)
            }
            override fun onError(t: Throwable?) {
                onLoadError(R.string.error404)
            }
        })
    }

    /**
     * Return server version or null if still loading
     */
    fun getServerVersion(): String? {
        if (version == null) {
            version = VERSION_REPOSITORY.getVersion()
        }
        return version
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadSuccess(data: List<Summoner>?) {
        view!!.onSummonersLoaded(data!!)
    }

    override fun onLoadError(message: String?) {
        if (isViewAttached) {
            view!!.onSummonersEmpty(R.string.error404)
        }
    }

    override fun onLoadError(resId: Int) {
        if (isViewAttached) {
            view!!.onSummonersEmpty(resId)
        }
    }

    override fun onLoadProgressChange(p0: String?) {
    }

    override fun onLoadProgressChange(p0: Int) {
    }

    //endregion

}