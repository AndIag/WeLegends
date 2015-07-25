package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampioDto implements Serializable {

    private ArrayList<String> allytips;
    private String blurb;
    private ArrayList<String> enemytips;
    private int id;
    private ImageDto image;
    private InfoDto info;
    private String key;
    private String lore;
    private String name;
    private String partype;
    private PassiveDto passive;
    //private ArrayList<RecommendedDto> recommended;  Recomended objects for the champion
    private ArrayList<SkinDto> skins;
    private ArrayList<ChampionSpellDto> spells;
    private StatsDto stats;
    private ArrayList<String> tags;
    private String title;

    public ChampioDto() {
    }

    public ArrayList<String> getAllytips() {
        return allytips;
    }

    public void setAllytips(ArrayList<String> allytips) {
        this.allytips = allytips;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public ArrayList<String> getEnemytips() {
        return enemytips;
    }

    public void setEnemytips(ArrayList<String> enemytips) {
        this.enemytips = enemytips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public InfoDto getInfo() {
        return info;
    }

    public void setInfo(InfoDto info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartype() {
        return partype;
    }

    public void setPartype(String partype) {
        this.partype = partype;
    }

    public PassiveDto getPassive() {
        return passive;
    }

    public void setPassive(PassiveDto passive) {
        this.passive = passive;
    }

    public ArrayList<SkinDto> getSkins() {
        return skins;
    }

    public void setSkins(ArrayList<SkinDto> skins) {
        this.skins = skins;
    }

    public ArrayList<ChampionSpellDto> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<ChampionSpellDto> spells) {
        this.spells = spells;
    }

    public StatsDto getStats() {
        return stats;
    }

    public void setStats(StatsDto stats) {
        this.stats = stats;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
