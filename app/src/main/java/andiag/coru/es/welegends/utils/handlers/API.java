package andiag.coru.es.welegends.utils.handlers;

/**
 * Created by Iago on 26/06/2015.
 */
public abstract class API {

    private static final String WELEGENDS_PROXY = "https://andiag-prod.apigee.net/v1/welegends/";
    private static final String DDRAGON_SERVER = "";
    private static final String MATCH_HISTORY = "/matches/";
    private static final String MATCH = "/match/";
    private static final String SUMMONER = "/summoner/";
    private static final String PROFILEICON = "/profileicon/";
    private static final String STATS = "/stats/";
    private static final String LEAGUES = "/leagues/";
    private static final String RECENT_GAMES = "/games/";
    private static final String CURRENG_GAME = "/current/";
    private static final String CHAMPIONS = "/champion";
    private static final String VERSIONS = "/versions";

    public static String getWelegendsProxy() {
        return WELEGENDS_PROXY;
    }

    public static String getMatchHistory() {
        return MATCH_HISTORY;
    }

    public static String getMatch() {
        return MATCH;
    }

    public static String getSummoner() {
        return SUMMONER;
    }

    public static String getProfileicon() {
        return PROFILEICON;
    }

    public static String getStats() {
        return STATS;
    }

    public static String getLeagues() {
        return LEAGUES;
    }

    public static String getRecentGames() {
        return RECENT_GAMES;
    }

    public static String getChampions() {
        return CHAMPIONS;
    }

    public static String getVersions() {
        return VERSIONS;
    }

    public static String getCurrengGame() {
        return CURRENG_GAME;
    }
}
