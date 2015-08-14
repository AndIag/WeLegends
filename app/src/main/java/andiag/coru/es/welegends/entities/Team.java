package andiag.coru.es.welegends.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 22/04/2015.
 */
public class Team implements Serializable {

    private ArrayList<BannedChampion> bans;
    private int baronKills;
    private int dragonKills;
    private boolean firstBaron;
    private boolean firstBlood;
    private boolean firstDragon;
    private boolean firstInhibitor;
    private boolean firstTower;
    private int inhibitorKills;
    private int teamId;
    private int towerKills;
    private boolean winner;

    public Team() {
    }

    public ArrayList<BannedChampion> getBans() {
        return bans;
    }

    public void setBans(ArrayList<BannedChampion> bans) {
        this.bans = bans;
    }

    public int getBaronkills() {
        return baronKills;
    }

    public void setBaronkills(int baronKills) {
        this.baronKills = baronKills;
    }

    public int getDragonkills() {
        return dragonKills;
    }

    public void setDragonkills(int dragonKills) {
        this.dragonKills = dragonKills;
    }

    public boolean isFirstBaron() {
        return firstBaron;
    }

    public void setFirstBaron(boolean firstBaron) {
        this.firstBaron = firstBaron;
    }

    public boolean isFirstBlood() {
        return firstBlood;
    }

    public void setFirstBlood(boolean firstBlood) {
        this.firstBlood = firstBlood;
    }

    public boolean isFirstDragon() {
        return firstDragon;
    }

    public void setFirstDragon(boolean firstDragon) {
        this.firstDragon = firstDragon;
    }

    public boolean isFirstInhibitor() {
        return firstInhibitor;
    }

    public void setFirstInhibitor(boolean firstInhibitor) {
        this.firstInhibitor = firstInhibitor;
    }

    public boolean isFirstTower() {
        return firstTower;
    }

    public void setFirstTower(boolean firstTower) {
        this.firstTower = firstTower;
    }

    public int getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(int inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTowerKills() {
        return towerKills;
    }

    public void setTowerKills(int towerKills) {
        this.towerKills = towerKills;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}
