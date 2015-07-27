package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionDto implements Serializable {

    private int id;
    private String name;
    private String key;
    private String title;
    /*
    private ArrayList<String> allytips;
    private String blurb;
    private ArrayList<String> enemytips;
    private ImageDto image;
    private InfoDto info;
    private String lore;
    private String partype;
    private PassiveDto passive;
    private ArrayList<SkinDto> skins;
    private ArrayList<ChampionSpellDto> spells;
    private StatsDto stats;
    private ArrayList<String> tags;
    */
    //private ArrayList<RecommendedDto> recommended;  Recomended objects for the champion


    public ChampionDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
