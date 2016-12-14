package es.coru.andiag.welegends

import android.content.Context
import android.util.Log
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner


/**
 * Created by Canalejas on 09/12/2016.
 */

@Database(name = WeLegendsDatabase.NAME, version = WeLegendsDatabase.VERSION)
class WeLegendsDatabase {
    companion object {
        const val NAME: String = "WeLegendsDatabase"
        const val VERSION: Int = 1

        fun recreateDatabase(context: Context) {
            try {
                Log.i(PresenterFragmentFindSummoner.TAG, "Recreating Database 4new Version")
                FlowManager.getDatabase(WeLegendsDatabase.NAME).reset(context.applicationContext)
            } catch (e: InvalidDBConfiguration) {
                Log.i(PresenterFragmentFindSummoner.TAG, "Database did not exist")
            }
        }

    }
}
