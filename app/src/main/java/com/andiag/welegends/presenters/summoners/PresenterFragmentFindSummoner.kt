package com.andiag.welegends.presenters.summoners

import android.support.annotation.StringRes
import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.presenters.commons.PresenterSummonerLoader
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.FragmentFindSummoner
import com.andiag.welegends.models.database.Summoner as SummonerEntity


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : AIPresenter<ActivitySummoners, FragmentFindSummoner>(),
        PresenterSummonerLoader, AIInterfaceLoaderHandlerPresenter<String> {

    private val TAG = PresenterFragmentFindSummoner::class.java.simpleName

    var version: String? = null

    override fun onViewAttached() {
        /**
         * Try to retrieve server version.
         * After EPVersion.getVersion(this) execution 2 things could happen
         *      - We have server version loaded
         *      - We have null but a new callback was added to [EPVersion]
         */
        this.version = VersionRepository.getVersion(this)
    }

    /**
     * Try to find summoner in server
     */
    fun getSummonerByName(name: String, region: String) {
        SummonerRepository.getSummonerByName(this, name, region)
    }

    /**
     * Return server version or null if still loading
     */
    fun getServerVersion(): String? {
        if (version == null && !VersionRepository.isLoading()) {
            /**
             * See if static load has ended with null as param
             */
            version = VersionRepository.getVersion(null)
        }
        return version
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadSuccess(data: String?) {
        this.version = data
        if (isViewCreated) {
            view!!.onVersionLoaded(data!!)
        }
    }

    override fun onLoadProgressChange(message: String?) {
        if (isViewCreated && hasContext()) {
            view!!.onStaticDataLoadChange(message)
        }
    }

    override fun onLoadProgressChange(@StringRes resId: Int) {
        if (isViewCreated && hasContext()) {
            view!!.onStaticDataLoadChange(context.getString(resId))
        }
    }

    override fun onLoadError(message: String?) {
    }

    override fun onLoadError(resId: Int) {
    }
    //endregion

    //region PresenterSummonerLoader
    override fun onSummonerFound(summoner: SummonerEntity) {
        if (isViewCreated) {
            view!!.onSummonerFound(summoner)
        }
    }

    override fun onSummonerLoadError(resId: Int) {
        if (isViewCreated) {
            view!!.onSummonerNotFound(resId)
        }
    }

    override fun onSummonerLoadError(message: String) {
        if (isViewCreated) {
            view!!.onSummonerNotFound(message)
        }
    }
    //endregion

}