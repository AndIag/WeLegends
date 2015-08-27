package andiag.coru.es.welegends.utils.handlers;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class Images {

    private static DefaultHashMap<Integer, Integer> maps;

    static {
        initializeMaps();
    }

    private static void initializeMaps() {
        maps = new DefaultHashMap<>(R.drawable.summoner_rift);
        maps.put(11, R.drawable.summoner_rift);
        maps.put(10, R.drawable.twisted_treeline);
        maps.put(8, R.drawable.crystal_scar);
        maps.put(12, R.drawable.howling_abyss1);//Puede que este mal
        maps.put(14, R.drawable.butcher);
    }

    public static Integer getMap(int id) {
        return maps.get(id);
    }
}

