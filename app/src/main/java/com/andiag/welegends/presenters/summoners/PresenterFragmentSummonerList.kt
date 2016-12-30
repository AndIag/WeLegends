package com.andiag.welegends.presenters.summoners

import com.andiag.commons.interfaces.AIInterfaceLoaderHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.FragmentSummonerList
import com.andiag.welegends.models.EPSummoner as SummonerWrapper

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterFragmentSummonerList private constructor() : AIPresenter<ActivitySummoners, FragmentSummonerList>(), AIInterfaceLoaderHandlerPresenter<List<Summoner>> {
    private val TAG = PresenterFragmentFindSummoner::class.java.simpleName

    override fun onViewAttached() {
    }

    fun loadSummoners() {
        SummonerWrapper.getSummonerHistoric(this, 20)
    }

    //region AIInterfaceLoaderPresenter
    override fun onLoadSuccess(data: List<Summoner>) {
        view!!.onSummonersLoaded(data)
    }

    override fun onLoadProgressChange(message: String) {
    }

    override fun onLoadProgressChange(resId: Int) {
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