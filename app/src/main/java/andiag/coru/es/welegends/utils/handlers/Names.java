package andiag.coru.es.welegends.utils.handlers;

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
        mapsNames.put(11, R.string.rift);
        mapsNames.put(10, R.string.tt);
        mapsNames.put(8, R.string.cs);
        mapsNames.put(12, R.string.ha);//Puede que este mal
        mapsNames.put(14, R.string.butcher);
    }

    public static Integer getMapName(int id) {
        return mapsNames.get(id);
    }

    public static String getPlatformId(String region) {
        switch (region) {
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
