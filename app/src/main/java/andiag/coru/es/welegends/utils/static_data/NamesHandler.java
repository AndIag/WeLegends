package andiag.coru.es.welegends.utils.static_data;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class NamesHandler {

    private static DefaultHashMap<Integer, Integer> mapsNames;

    static {
        initializeMapNames();
    }

    public static void initializeMapNames() {
        mapsNames = new DefaultHashMap<>(R.string.notFoundError);
        mapsNames.put(11, R.string.rift);
        mapsNames.put(10, R.string.tt);
        mapsNames.put(8, R.string.cs);
        mapsNames.put(12, R.string.ha);//Puede que este mal
        mapsNames.put(14, R.string.butcher);
    }

    public static Integer getMapName(int id) {
        return mapsNames.get(id);
    }

}
