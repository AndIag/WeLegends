package andiag.coru.es.welegends.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.rest.entities.Summoner;

/**
 * Created by Canalejas on 06/06/2016.
 */
public class DBSummoner extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "Summoners";
    public static final String ID = "_id";
    public static final String RIOT_ID = "riotId";
    public static final String NAME = "name";
    public static final String REGION = "region";
    public static final String ICON_ID = "iconId";
    public static final String LEVEL = "summonerLevel";
    public static final String LAST_UPDATE = "lastUpdate";
    private static final String TAG = "DBSummoner";
    private static DBSummoner dbHelper = null;

    //TODO split this in DBHelper + DBMethods

    private DBSummoner(Context context) {
        super(context, GlobalDBSettings.DB_NAME, null, GlobalDBSettings.DB_VERSION);
    }

    public static DBSummoner getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBSummoner(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RIOT_ID + " INTEGER, " +
                ICON_ID + " INTEGER, " +
                NAME + " TEXT, " +
                REGION + " TEXT, " +
                LEVEL + " INTEGER, " +
                LAST_UPDATE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Summoner getSummonerByName(String summonerName, String region){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null) return null;

        String execute = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " = '" + summonerName.toLowerCase() +
                "' AND " + REGION + " = '" +  region.toLowerCase() + "'";
        Cursor cursor = db.rawQuery(execute, null);
        Summoner summoner = null;
        try {
            if (cursor.getCount() == 1 && cursor.moveToFirst()) {
                summoner = new Summoner();
                summoner.setLocalId(cursor.getLong(cursor.getColumnIndex(ID)));
                summoner.setId(cursor.getLong(cursor.getColumnIndex(RIOT_ID)));
                summoner.setName(Utils.toTitleCase(cursor.getString(cursor.getColumnIndex(NAME))));
                summoner.setRegion(cursor.getString(cursor.getColumnIndex(REGION)).toUpperCase());
                summoner.setProfileIconId(cursor.getLong(cursor.getColumnIndex(ICON_ID)));
                summoner.setSummonerLevel(cursor.getInt(cursor.getColumnIndex(LEVEL)));
                summoner.setLastUpdate(cursor.getLong(cursor.getColumnIndex(LAST_UPDATE)));
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

        String where = ID + "=" + summonerId;

        ContentValues newValues = new ContentValues();
        newValues.put(LAST_UPDATE, Calendar.getInstance().getTimeInMillis());

        return db.update(TABLE_NAME, newValues, where, null);
    }

    public long addSummoner(Summoner summoner){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null) return -1;

        //If summoner already exist update it
        if(summoner.getLocalId()!=null || getSummonerByName(summoner.getName(), summoner.getRegion())!=null){
            return (updateLastSummonerSearch(summoner.getLocalId())) > 0 ? summoner.getLocalId() : -1;
        }

        //Else -> Insert new summoner in db
        ContentValues newSummoner = new ContentValues();
        newSummoner.put(RIOT_ID, summoner.getId());
        newSummoner.put(NAME, summoner.getName().toLowerCase());
        newSummoner.put(REGION, summoner.getRegion().toLowerCase());
        newSummoner.put(ICON_ID, summoner.getProfileIconId());
        newSummoner.put(LEVEL, summoner.getSummonerLevel());
        newSummoner.put(LAST_UPDATE, Calendar.getInstance().getTimeInMillis());

        return db.insert(TABLE_NAME, null, newSummoner);
    }

    public List<Summoner> selectTopNSummoners(int n) {
        //TODO
        return null;
    }

    public List<Summoner> selectSummoners() {
        //TODO
        return null;
    }

}
