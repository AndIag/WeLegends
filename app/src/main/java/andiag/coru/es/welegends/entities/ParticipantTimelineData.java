package andiag.coru.es.welegends.entities;

import java.io.Serializable;

/**
 * Created by Iago on 09/07/2015.
 */
public class ParticipantTimelineData implements Serializable {

    private double zeroToTen;
    private double tenToTwenty;
    private double twentyToThirty;
    private double thirtyToEnd;

    public ParticipantTimelineData() {
    }

    public double getZeroToTen() {
        return zeroToTen;
    }

    public void setZeroToTen(double zeroToTen) {
        this.zeroToTen = zeroToTen;
    }

    public double getTenToTwenty() {
        return tenToTwenty;
    }

    public void setTenToTwenty(double tenToTwenty) {
        this.tenToTwenty = tenToTwenty;
    }

    public double getTwentyToThirty() {
        return twentyToThirty;
    }

    public void setTwentyToThirty(double twentyToThirty) {
        this.twentyToThirty = twentyToThirty;
    }

    public double getThirtyToEnd() {
        return thirtyToEnd;
    }

    public void setThirtyToEnd(double thirtyToEnd) {
        this.thirtyToEnd = thirtyToEnd;
    }
}
