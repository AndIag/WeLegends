package es.coru.andiag.welegends.models.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import es.coru.andiag.welegends.models.entities.Summoner
import java.util.*

/**
 * Created by Canalejas on 08/12/2016.
 */

class DBSummoner private constructor(context: Context) {

    private val TAG = DBSummoner::class.java.simpleName

    init {
        dbHelper = DBHelper.getInstance(context)
    }

    fun getSummoner(summonerName: String, region: String): Summoner? {
        val db = dbHelper!!.readableDatabase ?: return null

        val where = DBHelper.SUMMONER_NAME + " = ? AND " + DBHelper.SUMMONER_REGION + " = ?"
        var summoner: Summoner? = null

        db.query(DBHelper.SUMMONER_TABLE_NAME, null, where,
                arrayOf(summonerName.toUpperCase(), region.toUpperCase()), null, null, null).use { cursor ->
            if (cursor.count == 1 && cursor.moveToFirst()) {
                summoner = Summoner()
                summoner!!.localId = cursor.getLong(cursor.getColumnIndex(DBHelper._ID))
                summoner!!.id = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID))
                summoner!!.name = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))
                summoner!!.region = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION))
                summoner!!.profileIconId = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID))
                summoner!!.summonerLevel = cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL))
                summoner!!.lastUpdate = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE))
                Log.i(TAG, "Found: " + summoner!!.name!!)
            }
        }

        return summoner
    }

    private fun updateLastSummonerSearch(summonerId: Long?): Int {
        val db = dbHelper!!.writableDatabase ?: return -1

        val where = DBHelper._ID + " = ?"

        val newValues = ContentValues()
        newValues.put(DBHelper.SUMMONER_LAST_UPDATE, Calendar.getInstance().timeInMillis)

        return db.update(DBHelper.SUMMONER_TABLE_NAME, newValues, where,
                arrayOf(summonerId.toString()))

    }

    fun addSummoner(summoner: Summoner): Long {
        val db = dbHelper!!.writableDatabase ?: return -1

        //If summoner already exist update it
        if (summoner.localId != null || getSummoner(summoner.name!!, summoner.region!!) != null) {
            return if (updateLastSummonerSearch(summoner.localId) > 0)
                summoner.localId!!
            else
                -1
        }

        //Else -> Insert new summoner in db
        val newSummoner = ContentValues()
        newSummoner.put(DBHelper.SUMMONER_RIOT_ID, summoner.id)
        newSummoner.put(DBHelper.SUMMONER_NAME, summoner.name!!.toUpperCase())
        newSummoner.put(DBHelper.SUMMONER_REGION, summoner.region!!.toUpperCase())
        newSummoner.put(DBHelper.SUMMONER_ICON_ID, summoner.profileIconId)
        newSummoner.put(DBHelper.SUMMONER_LEVEL, summoner.summonerLevel)
        newSummoner.put(DBHelper.SUMMONER_LAST_UPDATE, Calendar.getInstance().timeInMillis)

        return db.insert(DBHelper.SUMMONER_TABLE_NAME, null, newSummoner)
    }

    fun getSummoners(limit: Int): List<Summoner>? {
        val db = dbHelper!!.readableDatabase ?: return null

        val summoners = ArrayList<Summoner>()

        var summoner: Summoner

        db.query(DBHelper.SUMMONER_TABLE_NAME, null, null, null, null, null,
                DBHelper.SUMMONER_LAST_UPDATE, limit.toString()).use { cursor ->
            while (cursor.moveToNext()) {
                summoner = Summoner()
                summoner.localId = cursor.getLong(cursor.getColumnIndex(DBHelper._ID))
                summoner.id = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID))
                summoner.name = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))
                summoner.region = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION)).toUpperCase()
                summoner.profileIconId = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID))
                summoner.summonerLevel = cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL))
                summoner.lastUpdate = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE))

                summoners.add(summoner)
                Log.i(TAG, "Found: " + summoner.name!!)
            }
        }

        return summoners
    }

    fun getSummoners(): List<Summoner>? {
        val db = dbHelper!!.readableDatabase ?: return null

        val summoners = ArrayList<Summoner>()

        var summoner: Summoner

        db.query(DBHelper.SUMMONER_TABLE_NAME, null, null, null, null, null,
                DBHelper.SUMMONER_LAST_UPDATE).use { cursor ->
            while (cursor.moveToNext()) {
                summoner = Summoner()
                summoner.localId = cursor.getLong(cursor.getColumnIndex(DBHelper._ID))
                summoner.id = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID))
                summoner.name = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))
                summoner.region = cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION))
                summoner.profileIconId = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID))
                summoner.summonerLevel = cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL))
                summoner.lastUpdate = cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE))

                summoners.add(summoner)
                Log.i(TAG, "Found: " + summoner.name!!)
            }
        }

        return summoners
    }

    companion object {
        private var dbSummoner: DBSummoner? = null
        private var dbHelper: DBHelper? = null

        fun getInstance(context: Context): DBSummoner {
            if (dbSummoner == null) {
                dbSummoner = DBSummoner(context)
            }
            return dbSummoner as DBSummoner
        }
    }
}