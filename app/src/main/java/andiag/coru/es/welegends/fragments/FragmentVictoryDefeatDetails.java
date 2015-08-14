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
import java.util.Set;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.adapters.AdapterTeamDetails;
import andiag.coru.es.welegends.entities.Participant;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/*
        team    Bundle
        0       Participant
        1       Participant
        2       Participant
        .
        .
        .
 */


public class FragmentVictoryDefeatDetails extends SwipeRefreshLayoutFragment implements NotifycableFragment {

    private static ActivityDetails activityMain;

    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterTeamDetails adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    private Bundle data;
    private boolean type;

    public FragmentVictoryDefeatDetails() {
    }

    public static FragmentVictoryDefeatDetails newInstance(boolean type) {
        FragmentVictoryDefeatDetails f = new FragmentVictoryDefeatDetails();
        Bundle arguments = new Bundle();
        arguments.putBoolean("type",type);
        f.setArguments(arguments);
        return f;
    }

    @Override
    public void notifyFragment() {
        if (type) {
            data = activityMain.getData(1);
        }
        if (data != null && adapter != null) {
            adapter.updateTeamMembers(parseData());
            changeRefreshingValue(false);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityDetails) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getBoolean("type");
        if (adapter == null) {
            adapter = new AdapterTeamDetails(activityMain,type);
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

    private ArrayList<Bundle> parseData() {
        ArrayList<Bundle> arrayList = new ArrayList<>();
        Participant p;
        Bundle b;

        Set<String> keys = data.keySet();
        for (int i = 0; i < keys.size() - 1; i++) {
            p = (Participant) data.getSerializable(String.valueOf(i));
            b = new Bundle();
            b.putSerializable("participant", p);
            arrayList.add(b);
        }

        b = data.getBundle("team");
        arrayList.add(0, b);

        return arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type) {
            data = activityMain.getData(1);
        }
        if (data != null && adapter != null) {
            adapter.updateTeamMembers(parseData());
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

}
