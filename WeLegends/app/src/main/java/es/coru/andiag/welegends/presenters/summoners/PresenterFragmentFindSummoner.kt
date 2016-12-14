package es.coru.andiag.welegends.presenters.summoners

import android.content.Context
import es.coru.andiag.andiag_mvp.base.BaseFragmentPresenter
import es.coru.andiag.welegends.models.Summoner
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentFindSummoner
import es.coru.andiag.welegends.models.wrapped.database.Summoner as SummonerEntity


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFragmentFindSummoner : BaseFragmentPresenter<FragmentFindSummoner, ActivitySummoners>(), SummonerDataLoaderPresenter {

    override fun onViewAttached() {
        Version.checkServerVersion(this)
    }

    override fun onViewDetached() {

    }

    fun getSummonerByName(name: String, region: String) {
        Summoner.getSummonerByName(this, name, region)
    }

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

    override fun onSummonerFound(summoner: SummonerEntity) {
        view!!.onSummonerFound(summoner)
    }

    override fun onSummonerLoadError(message: Int) {
        view!!.onSummonerNotFound(message)
        //TODO give some type of feedback
    }

    companion object {
        internal val TAG = PresenterFragmentFindSummoner::class.java.simpleName
    }

}