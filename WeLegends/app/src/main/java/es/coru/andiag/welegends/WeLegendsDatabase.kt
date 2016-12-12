package es.coru.andiag.welegends

import com.raizlabs.android.dbflow.annotation.Database


/**
 * Created by Canalejas on 09/12/2016.
 */

@Database(name = WeLegendsDatabase.NAME, version = WeLegendsDatabase.VERSION)
class WeLegendsDatabase {
    companion object {
        const val NAME: String = "WeLegendsDatabase"
        const val VERSION: Int = 1
    }
}
