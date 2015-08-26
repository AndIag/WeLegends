package andiag.coru.es.welegends.utils.handlers;

/**
 * Created by Iago on 26/06/2015.
 */
public abstract class API {

    public static final String SPLASH = "_0.jpg";
    public static final String SKIN = "_1.jpg";
    //WeLegendsProxy
    private static final String WELEGENDS_PROXY = "http://andiag-prod.apigee.net/v1/welegends/";
    //private static final String WELEGENDS_PROXY = "http://andiag-test.apigee.net/v1/welegends/";
    private static final String MATCH = "/match/";
    private static final String HISTORY = "/history";
    private static final String RANKEDS = "/rankeds/";
    private static final String CURRENT = "/current/";
    private static final String SUMMONER = "/summoner/";
    private static final String LEAGUES = "/leagues";
    private static final String STATS = "/stats";
    //Ddragon
    private static final String DDRAGON_SERVER = "http://ddragon.leagueoflegends.com/cdn/";
    private static final String CHAMPION_ICON = "/img/champion/";
    private static final String CHAMPION_SPLASH = "/img/champion/splash/";
    private static final String PROFILEICON = "/img/profileicon/";
    private static final String ITEM = "/img/item/";
    //Static
    private static final String VERSION = "http://andiag-prod.apigee.net/v1/welegends/versions";
    private static final String ALL_CHAMPS_DATA = "http://andiag-prod.apigee.net/v1/welegends/champion";
    //Formats
    private static final String PNG = ".png";

    public static String getMatch(String region, long id){
        return WELEGENDS_PROXY + region + MATCH + id;
    }

    public static String getHistory(String region, long id){
        return WELEGENDS_PROXY + region + MATCH + id + HISTORY;
    }

    public static String getRankeds(String region, long id, int beginIndex, int endindex){
        return WELEGENDS_PROXY + region + MATCH + id + RANKEDS + beginIndex + "/" + endindex;
    }

    public static String getCurrent(String region, long id, String platform){
        return WELEGENDS_PROXY + region + MATCH + id + CURRENT + platform;
    }

    public static String getSummonerId(String region, String id){
        return WELEGENDS_PROXY + region + SUMMONER + id;
    }

    public static String getLeagues(String region, long id){
        return WELEGENDS_PROXY + region + SUMMONER + id + LEAGUES;
    }

    public static String getStats(String region, long id){
        return WELEGENDS_PROXY + region + SUMMONER + id+ STATS;
    }

    public static String getChampionIcon(String champKey) {
        return DDRAGON_SERVER + Champions.getServerVersion() + CHAMPION_ICON + champKey + PNG;
    }

    public static String getChampionImage(String champKey, String format) {
        return DDRAGON_SERVER + CHAMPION_SPLASH + champKey + format;
    }

    public static String getProfileIcon(long iconId) {
        return DDRAGON_SERVER + Champions.getServerVersion() + PROFILEICON + iconId + PNG;
    }

    public static String getItemImage(String version, long id) {
        return DDRAGON_SERVER + version + ITEM + id + PNG;
    }

    public static String getAllChampsData() {
        return ALL_CHAMPS_DATA;
    }

    public static String getVersions() {
        return VERSION;
    }

}
