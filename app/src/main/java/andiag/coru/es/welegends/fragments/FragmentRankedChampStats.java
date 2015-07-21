package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterRankedChamps;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentRankedChampStats extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    private ObservableRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    //METRICS
    private DisplayMetrics outMetrics;
    private Display display;

    private AdapterRankedChamps adapter;

    public static FragmentRankedChampStats newInstance() {
        FragmentRankedChampStats fragmentRankedChampStats = new FragmentRankedChampStats();
        Bundle args = new Bundle();
        fragmentRankedChampStats.setArguments(args);
        return fragmentRankedChampStats;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (getArguments() != null) {

        }
        if (savedInstanceState != null) { //Load saved data in onPause

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityMain) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);
        adapter = new AdapterRankedChamps(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranked_champions_stats, container, false);

        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.scroll);

        initializeRefresh(rootView);

        recyclerView.setHasFixedSize(true);

        display = activityMain.getWindowManager().getDefaultDisplay();
        if (outMetrics == null) {
            outMetrics = new DisplayMetrics();
        }
        display.getMetrics(outMetrics);

        float density = activityMain.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);

        layoutManager = new GridLayoutManager(activityMain, columns);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
