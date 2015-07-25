package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 25/07/2015.
 */
public class SpellVarsDto implements Serializable {
    private ArrayList<Double> coeff;
    private String dyn;
    private String key;
    private String link;
    private String ranksWith;

    public SpellVarsDto() {
    }

    public ArrayList<Double> getCoeff() {
        return coeff;
    }

    public void setCoeff(ArrayList<Double> coeff) {
        this.coeff = coeff;
    }

    public String getDyn() {
        return dyn;
    }

    public void setDyn(String dyn) {
        this.dyn = dyn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRanksWith() {
        return ranksWith;
    }

    public void setRanksWith(String ranksWith) {
        this.ranksWith = ranksWith;
    }
}
