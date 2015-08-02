package andiag.coru.es.welegends.utils;

import andiag.coru.es.welegends.R;

/**
 * Created by iagoc on 03/08/2015.
 */
public class StatsColor {

    public static final int DEATHS = 0;
    public static final int KDA = 1;
    public static final int PERCENT = 2;
    public static final int GOLD = 3;
    public static final int CS = 4;

    public static int getColor(int mode, float value){
        int color = R.color.stats_bad;
        switch (mode) {
            case DEATHS:
                if(value>8) color = R.color.stats_bad;
                if(value>5 && value<=8) color = R.color.stats_regular;
                if(value>2 && value<=5) color = R.color.stats_good;
                if(value<=2) color = R.color.stats_perfect;
                break;
            case KDA:
                if(value<=1) color = R.color.stats_bad;
                if(value>1 && value<=2) color = R.color.stats_regular;
                if(value>2 && value<=5) color = R.color.stats_good;
                if(value>6) color = R.color.stats_perfect;
                break;
            case PERCENT:
                if(value<50) color = R.color.stats_bad;
                if(value>=50 && value<60) color = R.color.stats_regular;
                if(value>=60 && value<85) color = R.color.stats_good;
                if(value>=85) color = R.color.stats_perfect;
                break;
            case GOLD:
                if(value<10000) color = R.color.stats_bad;
                if(value>=10000 && value<13000) color = R.color.stats_regular;
                if(value>=13000 && value<16000) color = R.color.stats_good;
                if(value>=16000) color = R.color.stats_perfect;
                break;
            case CS:
                if(value<100) color = R.color.stats_bad;
                if(value>=100 && value<200) color = R.color.stats_regular;
                if(value>=200 && value<230) color = R.color.stats_good;
                if(value>=230) color = R.color.stats_perfect;
                break;
        }
        return color;
    }


}
