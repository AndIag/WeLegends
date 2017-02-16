package com.andiag.welegends.views.summoners.search

import android.support.design.widget.Snackbar
import android.util.Log
import com.andiag.core.presenters.AIPresenter
import com.andiag.welegends.R
import com.andiag.welegends.common.base.ActivityBase
import com.andiag.welegends.common.utils.CallbackData
import com.andiag.welegends.models.*
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners


/**
 * Created by andyq on 09/12/2016.
 */
class PresenterFindSummoner(summonersRepository: ISummonerRepository)
    : AIPresenter<ActivitySummoners, FragmentFindSummoner>() {

    private val TAG = PresenterFindSummoner::class.java.simpleName
    private lateinit var mVersionRepository: IVersionRepository
    private val mSummonerRepository: ISummonerRepository = summonersRepository

    constructor() : this(SummonerRepository.getInstance())

    override fun onViewAttached() {
        mVersionRepository = VersionRepository.getInstance(context)
        mVersionRepository.read(LoadingType.REMOTE_LOAD, object : CallbackData<String> {
            override fun onSuccess(data: String?) {
                if (isViewCreated) {
                    view!!.onVersionLoaded(data!!)
                }
            }

            override fun onError(t: Throwable?) {
                Log.e(TAG, t!!.message)
                Snackbar.make(view.view!!, R.string.error_loading_static_data, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry, {
                            mVersionRepository.read(LoadingType.REMOTE_LOAD, this)
                        })
                        .setActionTextColor(ActivityBase.resolveColorAttribute(context, R.attr.mainColor))
                        .show()
            }

        })
    }

    fun isLoadingVersion(): Boolean {
        return mVersionRepository.isLoading()
    }

    //region Summoner

    fun getSummonerByName(name: String, region: String) {
        if (!name.isEmpty()) {
            // Try to find summoner
            mSummonerRepository.read(name, region, LoadingType.REMOTE_LOAD, object : CallbackData<Summoner> {
                override fun onSuccess(data: Summoner?) {
                    onSummonerFound(data!!, true)
                }

                override fun onError(t: Throwable?) {
                    onSummonerLoadError(R.string.error404)
                }
            })
        } else {
            Log.e(TAG, "Empty summoner")
            onSummonerLoadError(R.string.voidSummonerError)
        }
    }

    fun onSummonerFound(summoner: Summoner, isLocal: Boolean) {
        if (isViewCreated) {
            view!!.onSummonerFound(summoner, isLocal)
        }
    }

    fun onSummonerLoadError(resId: Int) {
        if (isViewCreated) {
            view!!.onSummonerNotFound(view!!.getString(resId))
        }
    }
    //endregion

}