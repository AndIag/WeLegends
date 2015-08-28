package andiag.coru.es.welegends.entities.DTOs.summonerSpellsDTOs;

import java.io.Serializable;

/**
 * Created by iagoc on 27/08/2015.
 */
public class SummonerSpellDto implements Serializable {
    private int id;
    private String key;
    private String name;

    /*
    private ArrayList<Double> cooldown;
    private String cooldownBurn;
    private ArrayList<Integer> cost;
    private String costBurn;
    private String costType;
    private String description;
    private ArrayList<ArrayList<Double>> effect;	//This field is a List of List of Double.
    private ArrayList<String> effectBurn;
    private ImageDto image;
    private LevelTipDto leveltip;
    private int maxrank;
    private ArrayList<String> modes;
    private Object range;	//This field is either a List of Integer or the String 'self' for spells that target one's own champion.
    private String rangeBurn;
    private String resource;
    private String sanitizedDescription;
    private String sanitizedTooltip;
    private int summonerLevel;
    private String tooltip;
    private ArrayList<SpellVarsDto> vars;
    */

    public SummonerSpellDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
