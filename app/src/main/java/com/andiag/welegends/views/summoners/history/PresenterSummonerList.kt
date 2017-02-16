package com.andiag.welegends.views.summoners.history

import com.andiag.commons.interfaces.presenters.AIErrorHandlerPresenter
import com.andiag.commons.interfaces.presenters.AISuccessHandlerPresenter
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.ISummonerRepository
import com.andiag.welegends.models.IVersionRepository
import com.andiag.welegends.models.SummonerRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.andiag.welegends.views.summoners.search.PresenterFindSummoner

/**
 * Created by andyq on 15/12/2016.
 */
class PresenterSummonerList
(summonersRepository: ISummonerRepository)
    : AIPresenter<ActivitySummoners, FragmentSummonerList>(),
        AISuccessHandlerPresenter<List<Summoner>>, AIErrorHandlerPresenter {

    private val TAG = PresenterFindSummoner::class.java.simpleName
    private lateinit var mVersionRepository: IVersionRepository
    private val mSummonerRepository: ISummonerRepository = summonersRepository

    constructor() : this(SummonerRepository.getInstance())

    override fun onViewAttached() {
        mVersionRepository = VersionRepository.getInstance(context)
    }

    fun loadSummoners() {
        mSummonerRepository.getSummonerHistoric(20, object : CallbackData<List<Summoner>?> {
            override fun onSuccess(data: List<Summoner>?) {
                this@PresenterSummonerList.onSuccess(data!!)
            }

            override fun onError(t: Throwable?) {
                this@PresenterSummonerList.onError(R.string.error404)
            }
        })
    }

    /**
     * Return server version or null if still loading
     */
    fun getVersion(): String {
        return mVersionRepository.syncRead()!!
    }

    //region AIInterfaceLoaderPresenter
    override fun onSuccess(data: List<Summoner>) {
        view!!.onSummonersLoaded(data)
    }

    override fun onError(resId: Int) {
        onError(context.getString(resId))
    }

    override fun onError(error: String?) {
        if (isViewAttached) {
            view!!.onSummonersEmpty(error)
        }
    }

    //endregion

}