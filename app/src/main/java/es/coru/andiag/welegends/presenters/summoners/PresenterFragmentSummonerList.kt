package es.coru.andiag.welegends.presenters.summoners

import android.content.Context
import es.coru.andiag.andiag_mvp.presenters.AIFragmentPresenter
import es.coru.andiag.andiag_mvp.presenters.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentSummonerList

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterFragmentSummonerList : AIFragmentPresenter<FragmentSummonerList, ActivitySummoners>(), AIInterfaceLoaderPresenter<List<Summoner>> {

    override fun onViewAttached() {
    }

    fun loadSummoners() {
        es.coru.andiag.welegends.models.Summoner.getSummonerHistoric(this, 20)
    }

    override fun onViewDetached() {
    }

    //region AIInterfaceLoaderPresenter
    override fun getContext(): Context {
        return parent!!
    }

    override fun onLoadSuccess(data: List<Summoner>) {
        view!!.onSummonersLoaded(data)
    }

    override fun onLoadProgressChange(message: String?) {
    }

    override fun onLoadError(message: String?) {
        view!!.onSummonersEmpty(R.string.error404)
    }

    override fun onLoadError(resId: Int) {
        view!!.onSummonersEmpty(resId)
    }
    //endregion
}