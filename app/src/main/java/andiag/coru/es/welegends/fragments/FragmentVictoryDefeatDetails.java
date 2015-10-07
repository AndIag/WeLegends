package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMatchDetails;
import andiag.coru.es.welegends.adapters.AdapterTeamDetails;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/*
        team            Team
        totalKills      int
        totalDeaths     int
        totalAssists    int

        participant     Participant
*/


public class FragmentVictoryDefeatDetails extends SwipeRefreshLayoutFragment implements NotifycableFragment {

    private static ActivityMatchDetails activityMain;

    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterTeamDetails adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    private ArrayList<Bundle> data;
    private boolean isWinner;

    public FragmentVictoryDefeatDetails() {
    }

    public static FragmentVictoryDefeatDetails newInstance(boolean type) {
        FragmentVictoryDefeatDetails f = new FragmentVictoryDefeatDetails();
        Bundle arguments = new Bundle();
        arguments.putBoolean("isWinner", type);
        f.setArguments(arguments);
        return f;
    }

    @Override
    public void notifyFragment() {
        data = getBundleData();
        if (data != null && adapter != null) {
            adapter.updateTeamMembers(data);
            scaleAdapter.notifyDataSetChanged();
            changeRefreshingValue(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityMatchDetails) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isWinner = getArguments().getBoolean("isWinner");
        if (adapter == null) {
            adapter = new AdapterTeamDetails(activityMain, isWinner);
            scaleAdapter = new ScaleInAnimationAdapter(adapter);
            scaleAdapter.setFirstOnly(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champions_stats, container, false);

        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.scroll);

        initializeRefresh(rootView);

        layoutManager = new LinearLayoutManager(activityMain);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(scaleAdapter);
        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        data = getBundleData();
        if (data != null && adapter != null) {
            adapter.updateTeamMembers(data);
            scaleAdapter.notifyDataSetChanged();
            changeRefreshingValue(false);
        }
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changeRefreshingValue(false);
            }
        });
        changeRefreshingValue(true);
    }

    private ArrayList<Bundle> getBundleData() {
        if (isWinner) {
            return activityMain.getWinnerTeam();
        } else {
            return activityMain.getLosserTeam();
        }
    }

}
