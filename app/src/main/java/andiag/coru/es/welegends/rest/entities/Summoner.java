package andiag.coru.es.welegends.rest.entities;

import java.io.Serializable;

/**
 * Created by Iago on 22/04/2015.
 */
public class Summoner implements Serializable {

    private long id;
    private Long localId = null;
    private String name;
    private String region;
    private long profileIconId;
    private Long lastUpdate = null;
    private int summonerLevel;

    public Summoner() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Summoner summoner = (Summoner) o;

        if (id != summoner.id) return false;
        if (profileIconId != summoner.profileIconId) return false;
        if (summonerLevel != summoner.summonerLevel) return false;
        if (!name.equals(summoner.name)) return false;
        return region.equals(summoner.region);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + region.hashCode();
        result = 31 * result + (int) (profileIconId ^ (profileIconId >>> 32));
        result = 31 * result + summonerLevel;
        return result;
    }
}
