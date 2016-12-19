package es.coru.andiag.welegends.presenters.summoners

import es.coru.andiag.andiag_mvp.presenters.AIPresenter
import es.coru.andiag.welegends.models.Summoner
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentFindSummoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner as SummonerEntity


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner private constructor() : AIPresenter<ActivitySummoners, FragmentFindSummoner>(), PresenterSummonerLoader {
    private val TAG = PresenterFragmentFindSummoner::class.java.simpleName

    var version: String? = null

    override fun onViewAttached() {
        /**
         * Try to retrieve server version.
         * After Version.getVersion(this) execution 2 things could happen
         *      - We have server version loaded
         *      - We have null but a new callback was added to [Version]
         */
        this.version = Version.getVersion(this)
    }

    /**
     * Try to find summoner in server
     */
    fun getSummonerByName(name: String, region: String) {
        Summoner.getSummonerByName(this, name, region)
    }

    /**
     * Return server version or null if still loading
     */
    fun getServerVersion(): String? {
        if (version == null && !Version.isLoading()) {
            /**
             * See if static load has ended with null as param
             */
            version = Version.getVersion(null)
        }
        return version
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadSuccess(data: String?) {
        this.version = data
        if (isViewAttached) {
            view!!.onVersionLoaded(data!!)
        }
    }

    override fun onLoadProgressChange(message: String, stillLoading: Boolean) {
        if (isViewAttached && hasContext()) {
            view!!.onStaticDataLoadChange(message, stillLoading)
        }
    }

    override fun onLoadProgressChange(resId: Int, stillLoading: Boolean) {
        if (isViewAttached && hasContext()) {
            view!!.onStaticDataLoadChange(context.getString(resId), stillLoading)
        }
    }

    override fun onLoadError(message: String?) {
    }

    override fun onLoadError(resId: Int) {
    }
    //endregion

    //region PresenterSummonerLoader
    override fun onSummonerFound(summoner: SummonerEntity) {
        if (isViewAttached) {
            view!!.onSummonerFound(summoner)
        }
    }

    override fun onSummonerLoadError(resId: Int) {
        if (isViewAttached) {
            view!!.onSummonerNotFound(resId)
        }
    }

    override fun onSummonerLoadError(message: String) {
        if (isViewAttached) {
            view!!.onSummonerNotFound(message)
        }
    }
    //endregion

    companion object {
        private var presenter: PresenterFragmentFindSummoner? = null

        fun getInstance(): PresenterFragmentFindSummoner {
            if (presenter == null) {
                presenter = PresenterFragmentFindSummoner()
            }
            return presenter!!
        }
    }

}