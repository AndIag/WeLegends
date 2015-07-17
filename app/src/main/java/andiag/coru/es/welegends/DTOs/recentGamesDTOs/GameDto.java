package andiag.coru.es.welegends.DTOs.recentGamesDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 17/07/2015.
 */
public class GameDto implements Serializable {

    private long gameId;
    private Boolean invalid;
    private String gameMode;
    private String gameType;
    private String subType;
    private int mapId;
    private int teamId;
    private int championId;
    private int spell1;
    private int spell2;
    private int level;
    private int ipEarned;
    private long createDate;
    private ArrayList<PlayerDto> fellowPlayers;
    private RawStatsDto stats;

    public GameDto() {
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public int getSpell1() {
        return spell1;
    }

    public void setSpell1(int spell1) {
        this.spell1 = spell1;
    }

    public int getSpell2() {
        return spell2;
    }

    public void setSpell2(int spell2) {
        this.spell2 = spell2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIpEarned() {
        return ipEarned;
    }

    public void setIpEarned(int ipEarned) {
        this.ipEarned = ipEarned;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public ArrayList<PlayerDto> getFellowPlayers() {
        return fellowPlayers;
    }

    public void setFellowPlayers(ArrayList<PlayerDto> fellowPlayers) {
        this.fellowPlayers = fellowPlayers;
    }

    public RawStatsDto getStats() {
        return stats;
    }

    public void setStats(RawStatsDto stats) {
        this.stats = stats;
    }
}
