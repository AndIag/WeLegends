package es.coru.andiag.welegends.common.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Canalejas on 08/12/2016.
 */

class DBHelper private constructor(context: Context) : SQLiteOpenHelper(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION) {

    val TAG = DBHelper::class.java.simpleName

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + SUMMONER_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SUMMONER_RIOT_ID + " INTEGER, " +
                SUMMONER_ICON_ID + " INTEGER, " +
                SUMMONER_NAME + " TEXT, " +
                SUMMONER_REGION + " TEXT, " +
                SUMMONER_LEVEL + " INTEGER, " +
                SUMMONER_LAST_UPDATE + " INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + SUMMONER_TABLE_NAME)
        onCreate(db)
    }

    companion object {
        const val DB_NAME = "andiag_welegends.sqlite"
        const val DB_VERSION = 2

        const val SUMMONER_TABLE_NAME = "Summoners"

        const val _ID = "_id"
        const val SUMMONER_RIOT_ID = "riotId"
        const val SUMMONER_NAME = "name"
        const val SUMMONER_REGION = "region"
        const val SUMMONER_ICON_ID = "iconId"
        const val SUMMONER_LEVEL = "summonerLevel"
        const val SUMMONER_LAST_UPDATE = "lastUpdate"

        private var dbHelper: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            if (dbHelper == null) {
                dbHelper = DBHelper(context)
            }
            return dbHelper as DBHelper
        }
    }
}
