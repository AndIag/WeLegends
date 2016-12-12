package es.coru.andiag.welegends

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowLog
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application() {

    private val TAG: String = WeLegends::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build())
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V)
    }
}
