package andiag.coru.es.welegends.utils.handlers;

import andiag.coru.es.welegends.R;

/**
 * Created by iagoc on 03/08/2015.
 */
public abstract class StatsColor {

    //This do not include matches longer than 100minutes
    public static int getDeathColor(float deaths, long matchDuration) {
        int pValue = 2;
        long timeInc;

        if (matchDuration / 60 >= 35) {
            timeInc = ((matchDuration / 60) % 10) - 3;
        } else timeInc = 0;

        if (deaths <= pValue + timeInc) return R.color.stats_perfect;
        pValue += 3;
        if (deaths <= pValue + timeInc) return R.color.stats_good;
        pValue += 3;
        if (deaths <= pValue + timeInc) return R.color.stats_regular;

        return R.color.stats_bad;
    }

    //Use this method to kda, kills, asists
    public static int getKDAColor(float kda) {
        int bValue = 1;

        if (kda <= bValue) return R.color.stats_bad;
        bValue += 1;
        if (kda <= bValue) return R.color.stats_regular;
        bValue += 3;
        if (kda <= bValue) return R.color.stats_good;

        return R.color.stats_perfect;
    }

    public static int getCSColor(float cs, long matchDuration, int role) {
        long perMinCS = 5;
        long timeInc = (matchDuration / 60);

        if (role == 4) {
            return R.color.stats_good;
        }

        if (role == 0) {
            perMinCS = perMinCS / 2;
        }

        if (cs <= perMinCS * timeInc) return R.color.stats_bad;
        perMinCS += 2;
        if (cs <= perMinCS * timeInc) return R.color.stats_regular;
        perMinCS += 3;
        if (cs <= perMinCS * timeInc) return R.color.stats_good;

        return R.color.stats_perfect;
    }

    public static int getGoldColor(float gold, long matchDuration, int role) {
        long perMinGold = 285;
        long timeInc = (matchDuration / 60);

        if (role == 4) {
            perMinGold = (8 * perMinGold) / 10;
        }

        if (gold <= perMinGold * timeInc) return R.color.stats_bad;
        perMinGold = 371;
        if (gold <= perMinGold * timeInc) return R.color.stats_regular;
        perMinGold = 457;
        if (gold <= perMinGold * timeInc) return R.color.stats_good;

        return R.color.stats_perfect;

    }

    public static int getPercentColor(float percent) {
        if (percent < 50) return R.color.stats_bad;
        if (percent >= 50 && percent < 60) return R.color.stats_regular;
        if (percent >= 60 && percent < 85) return R.color.stats_good;
        return R.color.stats_perfect;
    }

}
