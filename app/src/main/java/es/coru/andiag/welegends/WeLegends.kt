package es.coru.andiag.welegends

import android.app.Application
import android.content.Context
import android.net.NetworkInfo
import android.widget.Toast
import com.github.pwittchen.reactivenetwork.library.Connectivity
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application() {

    private val TAG: String = WeLegends::class.java.simpleName

    override fun onCreate() {
        super.onCreate()

        FlowManager.init(FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build())

        initNetworkObserver(this)

    }

    companion object {
        internal var connectivity: Connectivity? = null

        private fun initNetworkObserver(context: Context) {
            ReactiveNetwork.observeNetworkConnectivity(context)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { connectivity ->
                        WeLegends.connectivity = connectivity
                        if (connectivity.state != NetworkInfo.State.CONNECTED) {
                            Toast.makeText(context, R.string.error_network, Toast.LENGTH_LONG).show()
                        }
                    }
        }

        fun isNetworkAvailable(): Boolean {
            return connectivity != null && connectivity!!.state == NetworkInfo.State.CONNECTED
        }
    }

}
