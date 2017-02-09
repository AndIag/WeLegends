package com.andiag.welegends.presenters.summoners

import com.andiag.commons.interfaces.presenters.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.FragmentSummonerList

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterFragmentSummonerList : AIPresenter<ActivitySummoners, FragmentSummonerList>(), AIInterfaceLoaderHandlerPresenter<List<Summoner>> {
    private val TAG = PresenterFragmentFindSummoner::class.java.simpleName

    override fun onViewAttached() {
    }

    fun loadSummoners() {
        SummonerRepository.getSummonerHistoric(this, 20)
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

    companion object {
        private var presenter: PresenterFragmentSummonerList? = null

        fun getInstance(): PresenterFragmentSummonerList {
            if (presenter == null) {
                presenter = PresenterFragmentSummonerList()
            }
            return presenter!!
        }
    }

}