package es.coru.andiag.welegends.presenters.summoners

import es.coru.andiag.andiag_mvp.presenters.AIPresenter
import es.coru.andiag.andiag_mvp.utils.AIInterfaceLoaderPresenter
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.views.summoners.impl.ActivitySummoners
import es.coru.andiag.welegends.views.summoners.impl.FragmentSummonerList

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterFragmentSummonerList : AIPresenter<ActivitySummoners, FragmentSummonerList>(), AIInterfaceLoaderPresenter<List<Summoner>> {
    override fun onViewAttached() {
    }

    fun loadSummoners() {
        es.coru.andiag.welegends.models.Summoner.getSummonerHistoric(this, 20)
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadSuccess(data: List<Summoner>) {
        view!!.onSummonersLoaded(data)
    }

    override fun onLoadProgressChange(message: String, stillLoading: Boolean) {
    }

    override fun onLoadProgressChange(resId: Int, stillLoading: Boolean) {
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
    //endregion
}