package andiag.coru.es.welegends.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentRankedChampStats extends SwipeRefreshLayoutFragment {

    private static FragmentRankedChampStats fragmentRankedChampStats;
    private static ActivityMain activityMain;

    public FragmentRankedChampStats() {
    }

    public static void deleteFragment() {
        fragmentRankedChampStats = null;
    }

    public static FragmentRankedChampStats getInstance(ActivityMain aM) {
        activityMain = aM;
        if (fragmentRankedChampStats != null) {
            return fragmentRankedChampStats;
        }
        fragmentRankedChampStats = new FragmentRankedChampStats();
        return fragmentRankedChampStats;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranked_champions_stats, container, false);
        return rootView;
    }
}
