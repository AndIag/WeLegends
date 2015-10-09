package andiag.coru.es.welegends.utils.static_data;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class Names {

    private static DefaultHashMap<Integer, Integer> mapsNames;

    static {
        initializeMapNames();
    }

    public static void initializeMapNames() {
        mapsNames = new DefaultHashMap<>(R.string.error404);
        mapsNames.put(1, R.string.rift);
        mapsNames.put(2, R.string.rift);
        mapsNames.put(4, R.string.tt);
        mapsNames.put(8, R.string.cs);
        mapsNames.put(10, R.string.tt);
        mapsNames.put(11, R.string.rift);
        mapsNames.put(12, R.string.ha);//Puede que este mal
        mapsNames.put(14, R.string.butcher);
    }

    public static Integer getMapName(int id) {
        return mapsNames.get(id);
    }

    public static int getGameMode(String mode) {
        if (mode == null) {
            return -1;
        }
        if (mode.contains("BOT")) return R.string.bot_game;
        if (mode.contains("RANKED")) {
            if (mode.contains("5x5")) {
                if (mode.contains("SOLO")) return R.string.ranked_solo5x5;
                if (mode.contains("PREMADE")) return R.string.ranked_premade;
                if (mode.contains("TEAM")) return R.string.ranked_team;
            }
            if (mode.contains("3x3")) {
                if (mode.contains("PREMADE")) return R.string.ranked_premade;
                if (mode.contains("TEAM")) return R.string.ranked_team;
            }
        }
        if (mode.contains("NORMAL")) {
            if (mode.contains("DRAFT")) return R.string.normal_draft;
            return R.string.normal_game;
        }
        if (mode.contains("ODIN")) {
            if (mode.contains("DRAFT")) return R.string.odin_draft;
            return R.string.odin_game;
        }
        if (mode.contains("GROUP_FINDER")) return R.string.group_finder;
        if (mode.contains("ARAM")) {
            if (mode.contains("BILGEWATER")) return R.string.bilgewater_aram_game;
            return R.string.aram_game;
        }
        if (mode.contains("BILGEWATER")) return R.string.bilgewater_game;
        if (mode.contains("URF")) return R.string.urf_game;
        if (mode.contains("FIRSTBLOOD")) return R.string.firstblood_game;
        if (mode.contains("ONEFORALL")) {
            if (mode.contains("MIRRORMODE")) return R.string.oneforall_mirror_game;
            return R.string.oneforall_game;
        }
        if (mode.contains("HEXAKILL")) return R.string.hexakill_game;
        if (mode.contains("KING_PORO")) return R.string.king_poro_game;
        if (mode.contains("COUNTER_PICK")) return R.string.counter_pick_game;
        if (mode.contains("ASCENSION")) return R.string.ascension;
        if (mode.equals("SR_6x6")) return R.string.rift6x6;
        if (mode.equals("NONE")) return R.string.custom_game;
        return -1;
    }

    public static String getPlatformId(String region) {
        switch (region.toUpperCase()) {
            case "BR":
                return "BR1";
            case "EUNE":
                return "EUN1";
            case "EUW":
                return "EUW1";
            case "KR":
                return "KR";
            case "LAN":
                return "LA1";
            case "LAS":
                return "LA2";
            case "NA":
                return "NA1";
            case "OCE":
                return "OC1";
            case "TR":
                return "TR1";
            case "RU":
                return "RU";
            default:
                return "Null";
        }
    }

}
