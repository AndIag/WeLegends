package andiag.coru.es.welegends.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;

/**
 * Created by Iago on 13/07/2015.
 */
public class FragmentHistory extends SwipeRefreshLayoutFragment {

    private static FragmentHistory fragmentHistory;
    private static ActivityMain activityMain;

    public static void deleteFragment() {
        fragmentHistory = null;
    }

    public static Fragment getInstance(ActivityMain aM) {
        activityMain = aM;
        if (fragmentHistory != null) {
            return fragmentHistory;
        }
        fragmentHistory = new FragmentHistory();
        return fragmentHistory;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors

        changeRefreshingValue(true);
    }
}
