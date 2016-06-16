package andiag.coru.es.welegends.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Canalejas on 06/06/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    static final String DB_NAME = "andiag_welegends.sqlite";
    static final int DB_VERSION = 2;

    public static final String SUMMONER_TABLE_NAME = "Summoners";

    public static final String _ID = "_id";
    public static final String SUMMONER_RIOT_ID = "riotId";
    public static final String SUMMONER_NAME = "name";
    public static final String SUMMONER_REGION = "region";
    public static final String SUMMONER_ICON_ID = "iconId";
    public static final String SUMMONER_LEVEL = "summonerLevel";
    public static final String SUMMONER_LAST_UPDATE = "lastUpdate";

    private static DBHelper dbHelper = null;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SUMMONER_TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SUMMONER_RIOT_ID + " INTEGER, " +
                SUMMONER_ICON_ID + " INTEGER, " +
                SUMMONER_NAME + " TEXT, " +
                SUMMONER_REGION + " TEXT, " +
                SUMMONER_LEVEL + " INTEGER, " +
                SUMMONER_LAST_UPDATE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SUMMONER_TABLE_NAME);
        onCreate(db);
    }
}
