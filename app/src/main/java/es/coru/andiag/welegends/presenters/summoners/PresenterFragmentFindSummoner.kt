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

    }

    override fun onViewDetached() {

    }

    fun getSummonerByName(name: String, region: String) {
        view!!.showLoading()
        Summoner.getSummonerByName(this, name, region)
    }

    //region AIInterfaceLoaderPresenter
    override fun getContext(): Context {
        return parent!!
    }

    override fun onLoadSuccess(data: String?) {
        this.version = data
        view!!.onVersionUpdate(data!!)
        view!!.hideLoading()
    }

    override fun onLoadProgressChange(message: String?) {
        view!!.onVersionUpdate(message!!)
    }

    override fun onLoadError(message: String?) {
        view!!.errorLoading(message)
        view!!.hideLoading()
    }

    override fun onLoadError(resId: Int) {
        view!!.errorLoading(resId)
        view!!.hideLoading()
    }
    //endregion

    //region PresenterSummonerLoader
    override fun onSummonerFound(summoner: SummonerEntity) {
        view!!.onSummonerFound(summoner)
        view!!.hideLoading()
    }

    override fun onSummonerLoadError(resId: Int) {
        view!!.onSummonerNotFound(resId)
        view!!.hideLoading()
    }

    override fun onSummonerLoadError(message: String) {
        view!!.onSummonerNotFound(message)
        view!!.hideLoading()
    }
    //endregion

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }

}