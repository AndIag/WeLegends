package com.andiag.welegends.presenters.summoners

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.IVersionRepository
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.FragmentSummonerList

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterFragmentSummonerList : AIPresenter<ActivitySummoners, FragmentSummonerList>(), AIInterfaceLoaderHandlerPresenter<List<Summoner>> {
    private val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    private val VERSION_REPOSITORY: IVersionRepository = VersionRepository.getInstance()

    var version: String? = null

    override fun onViewAttached() {
    }

    fun loadSummoners() {
        SummonerRepository.getSummonerHistoric(this, 20)
    }

    /**
     * Return server version or null if still loading
     */
    fun getServerVersion(): String? {
        if (version == null && !VERSION_REPOSITORY.isLoading()) {
            /**
             * See if static load has ended with null as param
             */
            version = VERSION_REPOSITORY.getVersion(null)
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