package andiag.coru.es.welegends.DTOs.currentGame;

import java.io.Serializable;
import java.util.ArrayList;

import andiag.coru.es.welegends.entities.BannedChampion;

/**
 * Created by iagoc on 29/08/2015.
 */
public class CurrentGameInfo implements Serializable {

    private ArrayList<BannedChampion> bannedChampions;
    private long gameId;
    private long gameLength;
    private String gameMode;
    private long gameQueueConfigId;
    private long gameStartTime;
    private String gameType;
    private long mapId;
    private ArrayList<CurrentGameParticipant> participants;
    private String platformId;
    //private Observer observers;

    public CurrentGameInfo() {
    }

    public ArrayList<BannedChampion> getBannedChampions() {
        return bannedChampions;
    }

    public void setBannedChampions(ArrayList<BannedChampion> bannedChampions) {
        this.bannedChampions = bannedChampions;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGameLength() {
        return gameLength;
    }

    public void setGameLength(long gameLength) {
        this.gameLength = gameLength;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public long getGameQueueConfigId() {
        return gameQueueConfigId;
    }

    public void setGameQueueConfigId(long gameQueueConfigId) {
        this.gameQueueConfigId = gameQueueConfigId;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(long gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public ArrayList<CurrentGameParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<CurrentGameParticipant> participants) {
        this.participants = participants;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}
