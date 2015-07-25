package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 25/07/2015.
 */
public class InfoDto implements Serializable {

    private int attack;
    private int defense;
    private int difficulty;
    private int magic;

    public InfoDto() {
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }
}
