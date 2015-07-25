package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Iago on 25/07/2015.
 */
public class LevelTipDto implements Serializable {
    private ArrayList<String> effect;
    private ArrayList<String> label;

    public LevelTipDto() {
    }

    public ArrayList<String> getEffect() {
        return effect;
    }

    public void setEffect(ArrayList<String> effect) {
        this.effect = effect;
    }

    public ArrayList<String> getLabel() {
        return label;
    }

    public void setLabel(ArrayList<String> label) {
        this.label = label;
    }
}
