package andiag.coru.es.welegends.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.rest.entities.Summoner;

/**
 * Created by Canalejas on 14/06/2016.
 */
public class DBSummoner {

    private final static String TAG = "DBSummoner";
    private static DBSummoner dbSummoner = null;
    private static DBHelper dbHelper = null;

    private DBSummoner(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public static DBSummoner getInstance(Context context) {
        if (dbSummoner == null) {
            dbSummoner = new DBSummoner(context);
        }
        return dbSummoner;
    }

    public Summoner getSummonerByName(String summonerName, String region){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null) return null;

        String execute = "SELECT * FROM " + DBHelper.SUMMONER_TABLE_NAME + " WHERE " + DBHelper.SUMMONER_NAME + " = '" + summonerName.toLowerCase() +
                "' AND " + DBHelper.SUMMONER_REGION + " = '" + region.toLowerCase() + "'";
        Cursor cursor = db.rawQuery(execute, null);
        Summoner summoner = null;
        try {
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                summoner = new Summoner();
                summoner.setLocalId(cursor.getLong(cursor.getColumnIndex(DBHelper._ID)));
                summoner.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID)));
                summoner.setName(Utils.toTitleCase(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))));
                summoner.setRegion(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION)).toUpperCase());
                summoner.setProfileIconId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID)));
                summoner.setSummonerLevel(cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL)));
                summoner.setLastUpdate(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE)));
            }
            if (cursor.getCount() > 1) {
                Log.d(TAG, "INCONSISTENCIES ERROR IN SUMMONER DATABASE");
            }
        }finally {
            cursor.close();
        }
        return summoner;
    }

    private int updateLastSummonerSearch(Long summonerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db==null) return -1;

        String where = DBHelper._ID + "=" + summonerId;

        ContentValues newValues = new ContentValues();
        newValues.put(DBHelper.SUMMONER_LAST_UPDATE, Calendar.getInstance().getTimeInMillis());

        return db.update(DBHelper.SUMMONER_TABLE_NAME, newValues, where, null);
    }

    public long addSummoner(Summoner summoner){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db==null) return -1;

        //If summoner already exist update it
        if(summoner.getLocalId()!=null || getSummonerByName(summoner.getName(), summoner.getRegion())!=null){
            return (updateLastSummonerSearch(summoner.getLocalId())) > 0 ? summoner.getLocalId() : -1;
        }

        //Else -> Insert new summoner in db
        ContentValues newSummoner = new ContentValues();
        newSummoner.put(DBHelper.SUMMONER_RIOT_ID, summoner.getId());
        newSummoner.put(DBHelper.SUMMONER_NAME, summoner.getName().toLowerCase());
        newSummoner.put(DBHelper.SUMMONER_REGION, summoner.getRegion().toLowerCase());
        newSummoner.put(DBHelper.SUMMONER_ICON_ID, summoner.getProfileIconId());
        newSummoner.put(DBHelper.SUMMONER_LEVEL, summoner.getSummonerLevel());
        newSummoner.put(DBHelper.SUMMONER_LAST_UPDATE, Calendar.getInstance().getTimeInMillis());

        return db.insert(DBHelper.SUMMONER_TABLE_NAME, null, newSummoner);
    }

    public boolean deleteSummoner(Summoner summoner) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db == null) return false;
        int deleted = db.delete(DBHelper.SUMMONER_TABLE_NAME, DBHelper._ID + "=" + summoner.getLocalId(), null);
        if (deleted > 0) return true;
        else return false;
    }

    public List<Summoner> selectTopNSummoners(int n) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) return null;

        ArrayList<Summoner> summoners = new ArrayList<>();

        String select = "SELECT * FROM " + DBHelper.SUMMONER_TABLE_NAME + " ORDER BY " + DBHelper.SUMMONER_LAST_UPDATE + " LIMIT " + n;
        Cursor cursor = db.rawQuery(select, null);
        Summoner summoner;

        try {
            while (cursor.moveToNext()) {
                summoner = new Summoner();
                summoner.setLocalId(cursor.getLong(cursor.getColumnIndex(DBHelper._ID)));
                summoner.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID)));
                summoner.setName(Utils.toTitleCase(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))));
                summoner.setRegion(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION)).toUpperCase());
                summoner.setProfileIconId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID)));
                summoner.setSummonerLevel(cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL)));
                summoner.setLastUpdate(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE)));

                summoners.add(summoner);
            }
        } finally {
            cursor.close();
        }

        return summoners;
    }

    public List<Summoner> selectSummoners() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db == null) return null;

        ArrayList<Summoner> summoners = new ArrayList<>();

        String select = "SELECT * FROM " + DBHelper.SUMMONER_TABLE_NAME + " ORDER BY " + DBHelper.SUMMONER_LAST_UPDATE;
        Cursor cursor = db.rawQuery(select, null);
        Summoner summoner;

        try {
            while (cursor.moveToNext()) {
                summoner = new Summoner();
                summoner.setLocalId(cursor.getLong(cursor.getColumnIndex(DBHelper._ID)));
                summoner.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_RIOT_ID)));
                summoner.setName(Utils.toTitleCase(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_NAME))));
                summoner.setRegion(cursor.getString(cursor.getColumnIndex(DBHelper.SUMMONER_REGION)).toUpperCase());
                summoner.setProfileIconId(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_ICON_ID)));
                summoner.setSummonerLevel(cursor.getInt(cursor.getColumnIndex(DBHelper.SUMMONER_LEVEL)));
                summoner.setLastUpdate(cursor.getLong(cursor.getColumnIndex(DBHelper.SUMMONER_LAST_UPDATE)));

                summoners.add(summoner);
            }
        } finally {
            cursor.close();
        }

        return summoners;
    }

}
