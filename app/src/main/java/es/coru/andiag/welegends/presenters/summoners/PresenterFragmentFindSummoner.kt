package es.coru.andiag.welegends.presenters.summoners

import android.content.Context
import es.coru.andiag.andiag_mvp.presenters.AIFragmentPresenter
import es.coru.andiag.welegends.models.Summoner
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentFindSummoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner as SummonerEntity


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : AIFragmentPresenter<FragmentFindSummoner, ActivitySummoners>(), PresenterSummonerLoader {

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

    override fun onViewDetached() {

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
    override fun getContext(): Context {
        return parent!!
    }

    override fun onLoadSuccess(data: String?) {
        this.version = data
        if (view != null) {
            view!!.onVersionLoaded(data!!)
        }
    }

    override fun onLoadProgressChange(message: String, stillLoading: Boolean) {
        view!!.onStaticDataLoadChange(message, stillLoading)
    }

    override fun onLoadProgressChange(resId: Int, stillLoading: Boolean) {
        view!!.onStaticDataLoadChange(context.getString(resId), stillLoading)
    }

    override fun onLoadError(message: String?) {
    }

    override fun onLoadError(resId: Int) {
    }
    //endregion

    //region PresenterSummonerLoader
    override fun onSummonerFound(summoner: SummonerEntity) {
        view!!.onSummonerFound(summoner)
    }

    override fun onSummonerLoadError(resId: Int) {
        view!!.onSummonerNotFound(resId)
    }

    override fun onSummonerLoadError(message: String) {
        view!!.onSummonerNotFound(message)
    }
    //endregion

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }

}