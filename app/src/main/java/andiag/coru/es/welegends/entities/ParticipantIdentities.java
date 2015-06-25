package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Andy on 20/04/2015.
 */
public class ParticipantIdentities implements Serializable {

    private int participantId;
    private Player player;

    public ParticipantIdentities() {
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
