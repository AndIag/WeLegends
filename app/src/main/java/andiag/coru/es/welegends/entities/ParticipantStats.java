package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Andy on 20/04/2015.
 */
public class ParticipantStats implements Serializable {
    //KDA
    private long assists;
    private long deaths;
    private long kills;
    private long champLevel;
    //Kill Sprees
    private long doubleKills;
    private long tripleKills;
    private long quadraKills;
    private long pentaKills;
    private long killingSprees;
    private long largestKillingSpree;
    //First
    private boolean firstBloodKill;
    private boolean firstBloodAssist;
    private boolean firstInhibitorKill;
    private boolean firstInhibitorAssist;
    private boolean firstTowerKill;
    private boolean firstTowerAssist;
    //Gold
    private long goldEarned;
    private long goldSpent;
    //Items
    private long item0;
    private long item1;
    private long item2;
    private long item3;
    private long item4;
    private long item5;
    private long item6;
    //Farm and Structures
    private long inhibitorKills;
    private long minionsKilled;
    private long neutralMinionsKilled;
    private long neutralMinionsKilledEnemyJungle;
    private long neutralMinionsKilledTeamJungle;
    private long towerKills;
    //Wards
    private long sightWardsBoughtInGame;
    private long visionWardsBoughtInGame;
    private long wardsKilled;
    private long wardsPlaced;
    //Stats
    private long largestCriticalStrike;
    private long largestMultiKill;
    private long magicDamageDealt;
    private long magicDamageDealtToChampions;
    private long magicDamageTaken;
    private long physicalDamageDealt;
    private long physicalDamageDealtToChampions;
    private long physicalDamageTaken;
    private long trueDamageDealt;
    private long trueDamageDealtToChampions;
    private long trueDamageTaken;
    private long totalDamageDealt;
    private long totalDamageDealtToChampions;
    private long totalDamageTaken;
    private long totalHeal;
    private long totalTimeCrowdControlDealt;
    private long totalUnitsHealed;
    //Other
    private long unrealKills;
    private boolean winner;

    public ParticipantStats() {
    }

    public long getAssists() {
        return assists;
    }

    public void setAssists(long assists) {
        this.assists = assists;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public long getKills() {
        return kills;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public long getChampLevel() {
        return champLevel;
    }

    public void setChampLevel(long champLevel) {
        this.champLevel = champLevel;
    }

    public long getDoubleKills() {
        return doubleKills;
    }

    public void setDoubleKills(long doubleKills) {
        this.doubleKills = doubleKills;
    }

    public long getTripleKills() {
        return tripleKills;
    }

    public void setTripleKills(long tripleKills) {
        this.tripleKills = tripleKills;
    }

    public long getQuadraKills() {
        return quadraKills;
    }

    public void setQuadraKills(long quadraKills) {
        this.quadraKills = quadraKills;
    }

    public long getPentaKills() {
        return pentaKills;
    }

    public void setPentaKills(long pentaKills) {
        this.pentaKills = pentaKills;
    }

    public long getKillingSprees() {
        return killingSprees;
    }

    public void setKillingSprees(long killingSprees) {
        this.killingSprees = killingSprees;
    }

    public long getLargestKillingSpree() {
        return largestKillingSpree;
    }

    public void setLargestKillingSpree(long largestKillingSpree) {
        this.largestKillingSpree = largestKillingSpree;
    }

    public boolean isFirstBloodKill() {
        return firstBloodKill;
    }

    public void setFirstBloodKill(boolean firstBloodKill) {
        this.firstBloodKill = firstBloodKill;
    }

    public boolean isFirstBloodAssist() {
        return firstBloodAssist;
    }

    public void setFirstBloodAssist(boolean firstBloodAssist) {
        this.firstBloodAssist = firstBloodAssist;
    }

    public boolean isFirstInhibitorKill() {
        return firstInhibitorKill;
    }

    public void setFirstInhibitorKill(boolean firstInhibitorKill) {
        this.firstInhibitorKill = firstInhibitorKill;
    }

    public boolean isFirstInhibitorAssist() {
        return firstInhibitorAssist;
    }

    public void setFirstInhibitorAssist(boolean firstInhibitorAssist) {
        this.firstInhibitorAssist = firstInhibitorAssist;
    }

    public boolean isFirstTowerKill() {
        return firstTowerKill;
    }

    public void setFirstTowerKill(boolean firstTowerKill) {
        this.firstTowerKill = firstTowerKill;
    }

    public boolean isFirstTowerAssist() {
        return firstTowerAssist;
    }

    public void setFirstTowerAssist(boolean firstTowerAssist) {
        this.firstTowerAssist = firstTowerAssist;
    }

    public long getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(long goldEarned) {
        this.goldEarned = goldEarned;
    }

    public long getGoldSpent() {
        return goldSpent;
    }

    public void setGoldSpent(long goldSpent) {
        this.goldSpent = goldSpent;
    }

    public long getItem0() {
        return item0;
    }

    public void setItem0(long item0) {
        this.item0 = item0;
    }

    public long getItem1() {
        return item1;
    }

    public void setItem1(long item1) {
        this.item1 = item1;
    }

    public long getItem2() {
        return item2;
    }

    public void setItem2(long item2) {
        this.item2 = item2;
    }

    public long getItem3() {
        return item3;
    }

    public void setItem3(long item3) {
        this.item3 = item3;
    }

    public long getItem4() {
        return item4;
    }

    public void setItem4(long item4) {
        this.item4 = item4;
    }

    public long getItem5() {
        return item5;
    }

    public void setItem5(long item5) {
        this.item5 = item5;
    }

    public long getItem6() {
        return item6;
    }

    public void setItem6(long item6) {
        this.item6 = item6;
    }

    public long getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(long inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public long getMinionsKilled() {
        return minionsKilled;
    }

    public void setMinionsKilled(long minionsKilled) {
        this.minionsKilled = minionsKilled;
    }

    public long getNeutralMinionsKilled() {
        return neutralMinionsKilled;
    }

    public void setNeutralMinionsKilled(long neutralMinionsKilled) {
        this.neutralMinionsKilled = neutralMinionsKilled;
    }

    public long getNeutralMinionsKilledEnemyJungle() {
        return neutralMinionsKilledEnemyJungle;
    }

    public void setNeutralMinionsKilledEnemyJungle(long neutralMinionsKilledEnemyJungle) {
        this.neutralMinionsKilledEnemyJungle = neutralMinionsKilledEnemyJungle;
    }

    public long getNeutralMinionsKilledTeamJungle() {
        return neutralMinionsKilledTeamJungle;
    }

    public void setNeutralMinionsKilledTeamJungle(long neutralMinionsKilledTeamJungle) {
        this.neutralMinionsKilledTeamJungle = neutralMinionsKilledTeamJungle;
    }

    public long getTowerKills() {
        return towerKills;
    }

    public void setTowerKills(long towerKills) {
        this.towerKills = towerKills;
    }

    public long getSightWardsBoughtInGame() {
        return sightWardsBoughtInGame;
    }

    public void setSightWardsBoughtInGame(long sightWardsBoughtInGame) {
        this.sightWardsBoughtInGame = sightWardsBoughtInGame;
    }

    public long getVisionWardsBoughtInGame() {
        return visionWardsBoughtInGame;
    }

    public void setVisionWardsBoughtInGame(long visionWardsBoughtInGame) {
        this.visionWardsBoughtInGame = visionWardsBoughtInGame;
    }

    public long getWardsKilled() {
        return wardsKilled;
    }

    public void setWardsKilled(long wardsKilled) {
        this.wardsKilled = wardsKilled;
    }

    public long getWardsPlaced() {
        return wardsPlaced;
    }

    public void setWardsPlaced(long wardsPlaced) {
        this.wardsPlaced = wardsPlaced;
    }

    public long getLargestCriticalStrike() {
        return largestCriticalStrike;
    }

    public void setLargestCriticalStrike(long largestCriticalStrike) {
        this.largestCriticalStrike = largestCriticalStrike;
    }

    public long getLargestMultiKill() {
        return largestMultiKill;
    }

    public void setLargestMultiKill(long largestMultiKill) {
        this.largestMultiKill = largestMultiKill;
    }

    public long getMagicDamageDealt() {
        return magicDamageDealt;
    }

    public void setMagicDamageDealt(long magicDamageDealt) {
        this.magicDamageDealt = magicDamageDealt;
    }

    public long getMagicDamageDealtToChampions() {
        return magicDamageDealtToChampions;
    }

    public void setMagicDamageDealtToChampions(long magicDamageDealtToChampions) {
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
    }

    public long getMagicDamageTaken() {
        return magicDamageTaken;
    }

    public void setMagicDamageTaken(long magicDamageTaken) {
        this.magicDamageTaken = magicDamageTaken;
    }

    public long getPhysicalDamageDealt() {
        return physicalDamageDealt;
    }

    public void setPhysicalDamageDealt(long physicalDamageDealt) {
        this.physicalDamageDealt = physicalDamageDealt;
    }

    public long getPhysicalDamageDealtToChampions() {
        return physicalDamageDealtToChampions;
    }

    public void setPhysicalDamageDealtToChampions(long physicalDamageDealtToChampions) {
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
    }

    public long getPhysicalDamageTaken() {
        return physicalDamageTaken;
    }

    public void setPhysicalDamageTaken(long physicalDamageTaken) {
        this.physicalDamageTaken = physicalDamageTaken;
    }

    public long getTrueDamageDealt() {
        return trueDamageDealt;
    }

    public void setTrueDamageDealt(long trueDamageDealt) {
        this.trueDamageDealt = trueDamageDealt;
    }

    public long getTrueDamageDealtToChampions() {
        return trueDamageDealtToChampions;
    }

    public void setTrueDamageDealtToChampions(long trueDamageDealtToChampions) {
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
    }

    public long getTrueDamageTaken() {
        return trueDamageTaken;
    }

    public void setTrueDamageTaken(long trueDamageTaken) {
        this.trueDamageTaken = trueDamageTaken;
    }

    public long getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(long totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public long getTotalDamageDealtToChampions() {
        return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(long totalDamageDealtToChampions) {
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public long getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public void setTotalDamageTaken(long totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public long getTotalHeal() {
        return totalHeal;
    }

    public void setTotalHeal(long totalHeal) {
        this.totalHeal = totalHeal;
    }

    public long getTotalTimeCrowdControlDealt() {
        return totalTimeCrowdControlDealt;
    }

    public void setTotalTimeCrowdControlDealt(long totalTimeCrowdControlDealt) {
        this.totalTimeCrowdControlDealt = totalTimeCrowdControlDealt;
    }

    public long getTotalUnitsHealed() {
        return totalUnitsHealed;
    }

    public void setTotalUnitsHealed(long totalUnitsHealed) {
        this.totalUnitsHealed = totalUnitsHealed;
    }

    public long getUnrealKills() {
        return unrealKills;
    }

    public void setUnrealKills(long unrealKills) {
        this.unrealKills = unrealKills;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}
