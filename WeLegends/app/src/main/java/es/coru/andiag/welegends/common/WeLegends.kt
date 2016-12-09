package es.coru.andiag.welegends.common

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application() {

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build())
    }
}
