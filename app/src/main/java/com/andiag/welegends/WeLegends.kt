package com.andiag.welegends

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application() {
    private val TAG: String = WeLegends::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Spiegel.otf")
                .setFontAttrId(R.attr.fontPath)
                .build())

        FlowManager.init(FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build())

    }
}
