package es.coru.andiag.welegends.common

import android.app.Application

import com.orm.SugarContext

/**
 * Created by Canalejas on 08/12/2016.
 */

class WeLegends : Application() {

    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
    }
}
