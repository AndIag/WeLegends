package andiag.coru.es.welegends.entities;

/**
 * Created by andyq on 16/07/2015.
 */
public class Entry {

    private Integer leaguePoints;
    private Boolean isFreshBlood;
    private Boolean isHotStreak;
    private String division;
    private Boolean isInactive;
    private Boolean isVeteran;
    private Integer losses;
    private String playerOrTeamName;
    private String playerOrTeamId;
    private Integer wins;

    public Integer getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(Integer leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public Boolean getIsFreshBlood() {
        return isFreshBlood;
    }

    public void setIsFreshBlood(Boolean isFreshBlood) {
        this.isFreshBlood = isFreshBlood;
    }

    public Boolean getIsHotStreak() {
        return isHotStreak;
    }

    public void setIsHotStreak(Boolean isHotStreak) {
        this.isHotStreak = isHotStreak;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Boolean getIsInactive() {
        return isInactive;
    }

    public void setIsInactive(Boolean isInactive) {
        this.isInactive = isInactive;
    }

    public Boolean getIsVeteran() {
        return isVeteran;
    }

    public void setIsVeteran(Boolean isVeteran) {
        this.isVeteran = isVeteran;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public String getPlayerOrTeamName() {
        return playerOrTeamName;
    }

    public void setPlayerOrTeamName(String playerOrTeamName) {
        this.playerOrTeamName = playerOrTeamName;
    }

    public String getPlayerOrTeamId() {
        return playerOrTeamId;
    }

    public void setPlayerOrTeamId(String playerOrTeamId) {
        this.playerOrTeamId = playerOrTeamId;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

}
