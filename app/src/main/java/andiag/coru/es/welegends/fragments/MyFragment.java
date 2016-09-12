package andiag.coru.es.welegends.fragments;

import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Canalejas on 12/09/2016.
 */
public class MyFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
