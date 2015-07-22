package andiag.coru.es.welegends.utils.static_data;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.DefaultHashMap;

/**
 * Created by Iago on 25/06/2015.
 */
public abstract class NamesHandler {

    private static DefaultHashMap<Integer, String> champNames;
    private static DefaultHashMap<Integer, Integer> mapsNames;

    static {
        initializeChampionNames();
        initializeMapNames();
    }

    public static void initializeMapNames() {
        mapsNames = new DefaultHashMap<>(R.string.notFoundError);
        mapsNames.put(11, R.string.rift);
        mapsNames.put(10, R.string.tt);
        mapsNames.put(8, R.string.cs);
        mapsNames.put(12, R.string.ha);//Puede que este mal
        mapsNames.put(14, R.string.ha);
    }

    private static void initializeChampionNames() {
        champNames = new DefaultHashMap<>("Unknown");
        champNames.put(266, "Aatrox");
        champNames.put(103, "Ahri");
        champNames.put(84, "Akali");
        champNames.put(12, "Alistar");
        champNames.put(32, "Amumu");
        champNames.put(34, "Anivia");
        champNames.put(1, "Annie");
        champNames.put(22, "Ashe");
        champNames.put(268, "Azir");
        champNames.put(432, "Bard");
        champNames.put(53, "Blitzcrank");
        champNames.put(63, "Brand");
        champNames.put(201, "Braum");
        champNames.put(51, "Caitlyn");
        champNames.put(69, "Cassiopeia");
        champNames.put(31, "Cho'Gath");
        champNames.put(42, "Corki");
        champNames.put(122, "Darius");
        champNames.put(131, "Diana");
        champNames.put(36, "Dr.mundo");
        champNames.put(119, "Draven");
        champNames.put(60, "Elise");
        champNames.put(28, "Evelynn");
        champNames.put(81, "Ezreal");
        champNames.put(9, "Fiddlesticks");
        champNames.put(114, "Fiora");
        champNames.put(105, "Fizz");
        champNames.put(3, "Galio");
        champNames.put(41, "Gangplank");
        champNames.put(86, "Garen");
        champNames.put(150, "Gnar");
        champNames.put(79, "Gragas");
        champNames.put(104, "Graves");
        champNames.put(120, "Hecarim");
        champNames.put(74, "Heimerdinger");
        champNames.put(39, "Irelia");
        champNames.put(40, "Janna");
        champNames.put(59, "Jarvan IV");
        champNames.put(24, "Jax");
        champNames.put(126, "Jayce");
        champNames.put(222, "Jinx");
        champNames.put(429, "Kalista");
        champNames.put(43, "Karma");
        champNames.put(30, "Karthus");
        champNames.put(38, "Kassadin");
        champNames.put(55, "Katarina");
        champNames.put(10, "Kayle");
        champNames.put(85, "Kennen");
        champNames.put(121, "Kha'Zix");
        champNames.put(96, "Kog'Maw");
        champNames.put(7, "Le Blanc");
        champNames.put(64, "Lee Sin");
        champNames.put(89, "Leona");
        champNames.put(127, "Lissandra");
        champNames.put(236, "Lucian");
        champNames.put(117, "Lulu");
        champNames.put(99, "Lux");
        champNames.put(54, "Malphite");
        champNames.put(90, "Malzahar");
        champNames.put(57, "Maokai");
        champNames.put(11, "Master Yi");
        champNames.put(21, "Miss Fortune");
        champNames.put(82, "Mordekaiser");
        champNames.put(25, "Morgana");
        champNames.put(267, "Nami");
        champNames.put(75, "Nasus");
        champNames.put(111, "Nautilus");
        champNames.put(76, "Nidalee");
        champNames.put(56, "Nocturne");
        champNames.put(20, "Nunu");
        champNames.put(2, "Olaf");
        champNames.put(61, "Orianna");
        champNames.put(80, "Pantheon");
        champNames.put(78, "Poppy");
        champNames.put(133, "Quinn");
        champNames.put(33, "Rammus");
        champNames.put(421, "Rek'Sai");
        champNames.put(58, "Renekton");
        champNames.put(107, "Rengar");
        champNames.put(92, "Riven");
        champNames.put(68, "Rumble");
        champNames.put(13, "Ryze");
        champNames.put(113, "Sejuani");
        champNames.put(35, "Shaco");
        champNames.put(98, "Shen");
        champNames.put(102, "Shyvana");
        champNames.put(27, "Singed");
        champNames.put(14, "Sion");
        champNames.put(15, "Sivir");
        champNames.put(72, "Skarner");
        champNames.put(37, "Sona");
        champNames.put(16, "Soraka");
        champNames.put(50, "Swain");
        champNames.put(134, "Syndra");
        champNames.put(91, "Talon");
        champNames.put(44, "Taric");
        champNames.put(17, "Teemo");
        champNames.put(412, "Thresh");
        champNames.put(18, "Tristana");
        champNames.put(48, "Trundle");
        champNames.put(23, "Tryndamere");
        champNames.put(4, "Twisted Fate");
        champNames.put(29, "Twitch");
        champNames.put(77, "Udyr");
        champNames.put(6, "Urgot");
        champNames.put(110, "Varus");
        champNames.put(67, "Vayne");
        champNames.put(45, "Veigar");
        champNames.put(161, "Vel'Koz");
        champNames.put(254, "Vi");
        champNames.put(112, "Viktor");
        champNames.put(8, "Vladimir");
        champNames.put(106, "Volibear");
        champNames.put(19, "Warwick");
        champNames.put(62, "Wukong");
        champNames.put(101, "Xerath");
        champNames.put(5, "Xinzhao");
        champNames.put(157, "Yasuo");
        champNames.put(83, "Yorick");
        champNames.put(154, "Zac");
        champNames.put(238, "Zed");
        champNames.put(115, "Ziggs");
        champNames.put(26, "Zilean");
        champNames.put(143, "Zyra");
        champNames.put(245, "Ekko");
    }

    public static String getChampName(int id) {
        return champNames.get(id);
    }

    public static Integer getMapName(int id) {
        return mapsNames.get(id);
    }

}
