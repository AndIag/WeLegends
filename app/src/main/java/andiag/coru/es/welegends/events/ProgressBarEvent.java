package andiag.coru.es.welegends.events;

/**
 * Created by Canalejas on 12/09/2016.
 */
public class ProgressBarEvent extends MessageEvent {

    public static final String START = "START";
    public static final String STOP = "STOP";

    public ProgressBarEvent(String message) {
        super(message);
    }
}
