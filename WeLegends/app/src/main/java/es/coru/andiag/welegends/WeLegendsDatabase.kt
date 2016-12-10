package es.coru.andiag.welegends

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by Canalejas on 09/12/2016.
 */

@Database(name = WeLegendsDatabase.NAME, version = WeLegendsDatabase.VERSION)
class WeLegendsDatabase {
    companion object {
        const val NAME = "andiag_welegends"
        const val VERSION = 1
    }
}
