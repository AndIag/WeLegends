package andiag.coru.es.welegends.utils.Data;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class ImagesHandler {

    private static DefaultHashMap<Integer, Integer> champions;
    private static DefaultHashMap<Integer, Integer> spells;

    static {
        initializeChampions();
        initializeSpells();
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

    private static void initializeChampions() {
        champions = new DefaultHashMap<>(R.drawable.default0);
        champions.put(266, R.drawable.aatrox);
        champions.put(103, R.drawable.ahri);
        champions.put(84, R.drawable.akali);
        champions.put(12, R.drawable.alistar);
        champions.put(32, R.drawable.amumu);
        champions.put(34, R.drawable.anivia);
        champions.put(1, R.drawable.annie);
        champions.put(22, R.drawable.ashe);
        champions.put(268, R.drawable.azir);
        champions.put(432, R.drawable.bard);
        champions.put(53, R.drawable.blitzcrank);
        champions.put(63, R.drawable.brand);
        champions.put(201, R.drawable.braum);
        champions.put(51, R.drawable.caitlyn);
        champions.put(69, R.drawable.cassiopeia);
        champions.put(31, R.drawable.chogath);
        champions.put(42, R.drawable.corki);
        champions.put(122, R.drawable.darius);
        champions.put(131, R.drawable.diana);
        champions.put(36, R.drawable.drmundo);
        champions.put(119, R.drawable.draven);
        champions.put(60, R.drawable.elise);
        champions.put(28, R.drawable.evelynn);
        champions.put(81, R.drawable.ezreal);
        champions.put(9, R.drawable.fiddlesticks);
        champions.put(114, R.drawable.fiora);
        champions.put(105, R.drawable.fizz);
        champions.put(3, R.drawable.galio);
        champions.put(41, R.drawable.gangplank);
        champions.put(86, R.drawable.garen);
        champions.put(150, R.drawable.gnar);
        champions.put(79, R.drawable.gragas);
        champions.put(104, R.drawable.graves);
        champions.put(120, R.drawable.hecarim);
        champions.put(74, R.drawable.heimerdinger);
        champions.put(39, R.drawable.irelia);
        champions.put(40, R.drawable.diana);
        champions.put(59, R.drawable.jarvaniv);
        champions.put(24, R.drawable.jax);
        champions.put(126, R.drawable.jayce);
        champions.put(222, R.drawable.jinx);
        champions.put(429, R.drawable.kalista);
        champions.put(43, R.drawable.karma);
        champions.put(30, R.drawable.karthus);
        champions.put(38, R.drawable.kassadin);
        champions.put(55, R.drawable.katarina);
        champions.put(10, R.drawable.kayle);
        champions.put(85, R.drawable.kennen);
        champions.put(121, R.drawable.khazix);
        champions.put(96, R.drawable.kogmaw);
        champions.put(7, R.drawable.leblanc);
        champions.put(64, R.drawable.leesin);
        champions.put(89, R.drawable.leona);
        champions.put(127, R.drawable.lissandra);
        champions.put(236, R.drawable.lucian);
        champions.put(117, R.drawable.lulu);
        champions.put(99, R.drawable.lux);
        champions.put(54, R.drawable.malphite);
        champions.put(90, R.drawable.malzahar);
        champions.put(57, R.drawable.maokai);
        champions.put(11, R.drawable.masteryi);
        champions.put(21, R.drawable.missfortune);
        champions.put(82, R.drawable.mordekaiser);
        champions.put(25, R.drawable.morgana);
        champions.put(267, R.drawable.nami);
        champions.put(75, R.drawable.nasus);
        champions.put(111, R.drawable.nautilus);
        champions.put(76, R.drawable.nidalee);
        champions.put(56, R.drawable.nocturne);
        champions.put(20, R.drawable.nunu);
        champions.put(2, R.drawable.olaf);
        champions.put(61, R.drawable.orianna);
        champions.put(80, R.drawable.pantheon);
        champions.put(78, R.drawable.poppy);
        champions.put(133, R.drawable.quinn);
        champions.put(33, R.drawable.rammus);
        champions.put(421, R.drawable.reksai);
        champions.put(58, R.drawable.renekton);
        champions.put(107, R.drawable.rengar);
        champions.put(92, R.drawable.riven);
        champions.put(68, R.drawable.rumble);
        champions.put(13, R.drawable.ryze);
        champions.put(113, R.drawable.sejuani);
        champions.put(35, R.drawable.shaco);
        champions.put(98, R.drawable.shen);
        champions.put(102, R.drawable.shyvana);
        champions.put(27, R.drawable.singed);
        champions.put(14, R.drawable.sion);
        champions.put(15, R.drawable.sivir);
        champions.put(72, R.drawable.skarner);
        champions.put(37, R.drawable.sona);
        champions.put(16, R.drawable.soraka);
        champions.put(50, R.drawable.swain);
        champions.put(134, R.drawable.syndra);
        champions.put(91, R.drawable.talon);
        champions.put(44, R.drawable.taric);
        champions.put(17, R.drawable.teemo);
        champions.put(412, R.drawable.thresh);
        champions.put(18, R.drawable.tristana);
        champions.put(48, R.drawable.trundle);
        champions.put(23, R.drawable.tryndamere);
        champions.put(4, R.drawable.twistedfate);
        champions.put(29, R.drawable.twitch);
        champions.put(77, R.drawable.udyr);
        champions.put(6, R.drawable.urgot);
        champions.put(110, R.drawable.varus);
        champions.put(67, R.drawable.vayne);
        champions.put(45, R.drawable.veigar);
        champions.put(161, R.drawable.velkoz);
        champions.put(254, R.drawable.vi);
        champions.put(112, R.drawable.viktor);
        champions.put(8, R.drawable.vladimir);
        champions.put(106, R.drawable.volibear);
        champions.put(19, R.drawable.warwick);
        champions.put(62, R.drawable.monkeyking);
        champions.put(101, R.drawable.xerath);
        champions.put(5, R.drawable.xinzhao);
        champions.put(157, R.drawable.yasuo);
        champions.put(83, R.drawable.yorick);
        champions.put(154, R.drawable.zac);
        champions.put(238, R.drawable.zed);
        champions.put(115, R.drawable.ziggs);
        champions.put(26, R.drawable.zilean);
        champions.put(143, R.drawable.zyra);
        champions.put(245, R.drawable.ekko);
    }

    public static Integer getChamp(int id) {
        return champions.get(id);
    }

    public static Integer getItem(long id) {
        return null;
    }

    public static Integer getSpell(int id) {
        return spells.get(id);
    }
}

