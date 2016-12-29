package com.andiag.welegends

import android.app.Application
import android.content.Context
import android.net.NetworkInfo
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.andiag.commons.interfaces.AIInterfaceErrorHandlerPresenter
import com.andiag.welegends.models.Version
import com.github.pwittchen.reactivenetwork.library.Connectivity
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application(), AIInterfaceErrorHandlerPresenter {
    private val TAG: String = WeLegends::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Spiegel.otf")
                .setFontAttrId(R.attr.fontPath)
                .build())

        FlowManager.init(FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build())

        WeLegends.connectivity = Connectivity.create(this)
        initNetworkObserver(this)

        if (WeLegends.isNetworkAvailable()) {
            Log.i(TAG, "Checking Server Version")
            Version.checkServerVersion(this)
        }

    }

    override fun getContext(): Context {
        return this
    }

    /**
     * Required callbacks for [Version.checkServerVersion]
     */
    //region Callbacks
    override fun onLoadError(message: String?) {
        Log.e(TAG, message)
        if (view != null) {
            Snackbar.make(view!!, R.string.error_loading_static_data, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, { Version.checkServerVersion(this) })
            return
        }
        longToast(R.string.toast_error_loading_static_data)
    }

    override fun onLoadError(resId: Int) {
        Log.e(TAG, getString(resId))
        if (view != null) {
            Snackbar.make(view!!, R.string.error_loading_static_data, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, { Version.checkServerVersion(this) })
            return
        }
        longToast(R.string.toast_error_loading_static_data)
    }
    //endregion

    companion object {
        private var connectivity: Connectivity? = null
        internal var view: View? = null

        /**
         * Add a network listener for all application
         * @param [context] listener owner
         */
        private fun initNetworkObserver(context: Context) {
            ReactiveNetwork.observeNetworkConnectivity(context)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { connectivity ->
                        WeLegends.connectivity = connectivity
                        if (connectivity.state != NetworkInfo.State.CONNECTED) {
                            context.toast(R.string.networkUnavailable)
                        }
                    }
        }

        /**
         * Ask if network is available
         * @return [Boolean]
         */
        fun isNetworkAvailable(): Boolean {
            return connectivity != null && connectivity!!.state == NetworkInfo.State.CONNECTED
        }
    }

}
