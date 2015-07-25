package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 25/07/2015.
 */
public class ChampionSpellDto implements Serializable {
    private ArrayList<ImageDto> altimages;
    private ArrayList<Double> cooldown;
    private String cooldownBurn;
    private ArrayList<Integer> cost;
    private String costBurn;
    private String costType;
    private String description;
    private ArrayList<ArrayList<Double>> effect;
    private ArrayList<String> effectBurn;
    private ImageDto image;
    private String key;
    private LevelTipDto leveltip;
    private int maxrank;
    private String name;
    // RITO PLEASE range	object	This field is either a List of Integer or the String 'self' for spells that target one's own champion.
    private String rangeBurn;
    private String resource;
    private String sanitizedDescription;
    private String sanitizedTooltip;
    private String tooltip;
    private ArrayList<SpellVarsDto> vars;
}
