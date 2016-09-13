package andiag.coru.es.welegends.fragments;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by andyq on 13/09/2016.
 */
public abstract class FragmentEventBase extends FragmentBase {

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
}
