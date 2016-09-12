package andiag.coru.es.welegends.events;

/**
 * Created by Canalejas on 12/09/2016.
 */
public abstract class MessageEvent {
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
