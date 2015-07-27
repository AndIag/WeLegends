package andiag.coru.es.welegends.utils.static_data;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class ImagesHandler {

    private static DefaultHashMap<Integer, Integer> spells;
    private static DefaultHashMap<Integer, Integer> maps;

    static {
        initializeSpells();
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

    private static void initializeSpells() {
        spells = new DefaultHashMap<>(R.drawable.item_default);
        spells.put(1, R.drawable.cleanse);
        spells.put(2, R.drawable.clairvoyance);
        spells.put(3, R.drawable.exhaust);
        spells.put(4, R.drawable.flash);
        spells.put(6, R.drawable.ghost);
        spells.put(7, R.drawable.heal);
        spells.put(10, R.drawable.revive);
        spells.put(11, R.drawable.smite);
        spells.put(12, R.drawable.teleport);
        spells.put(13, R.drawable.clarity);
        spells.put(14, R.drawable.ignite);
        spells.put(17, R.drawable.garrison);
        spells.put(21, R.drawable.barrier);
    }

    public static Integer getSpell(int id) {
        return spells.get(id);
    }

    public static Integer getMap(int id) {
        return maps.get(id);
    }
}

