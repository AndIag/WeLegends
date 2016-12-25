package es.coru.andiag.welegends

import android.content.Context
import android.util.Log
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration


/**
 * Created by Canalejas on 09/12/2016.
 */

@Database(name = WeLegendsDatabase.NAME, version = WeLegendsDatabase.VERSION)
class WeLegendsDatabase {
    companion object {
        private val TAG: String = WeLegendsDatabase::class.java.simpleName
        const val NAME: String = "WeLegends"
        const val VERSION: Int = 1

        fun recreateStaticTables(context: Context) {
            try {
                Log.i(TAG, "Recreating Database 4new Version")
                FlowManager.getDatabase(WeLegendsDatabase.NAME).reset(context.applicationContext)
                // TODO make this recreate only static data
            } catch (e: InvalidDBConfiguration) {
                Log.i(TAG, "Database did not exist")
            }
        }

    }
}
