package andiag.coru.es.welegends.events;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Canalejas on 13/09/2016.
 */
public abstract class ProgressBarSemaphore {

    private static int loading = 0;

    public static synchronized void increase() {
        if (loading == 0) {
            EventBus.getDefault().post(new ProgressBarEvent(ProgressBarEvent.START));
        }
        loading += 1;
    }

    public static synchronized void decrease() {
        if (loading > 0) {
            loading -= 1;
        }
        if (loading == 0) {
            EventBus.getDefault().post(new ProgressBarEvent(ProgressBarEvent.STOP));
        }
    }

}
