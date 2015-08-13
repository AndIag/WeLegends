package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.adapters.AdapterTeamDetails;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentVictoryDefeatDetails extends NotifycableFragment {

    private static ActivityDetails activityMain;

    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterTeamDetails adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    public FragmentVictoryDefeatDetails() {
    }

    public static FragmentVictoryDefeatDetails newInstance() {
        return new FragmentVictoryDefeatDetails();
    }

    @Override
    public void notifyFragment() {
        Log.d("NOTIFICATION", "RECEIVED");
    }

    //SAVE AND RETRIEVE DATA
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) { //Load saved data in onPause
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
        onRetrieveInstanceState(savedInstanceState);
        if (adapter == null) {
            adapter = new AdapterTeamDetails(activityMain);
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

        ArrayList<Bundle> list = new ArrayList<>();
        list.add(new Bundle());
        list.add(new Bundle());
        adapter.updateTeamMembers(list);

        recyclerView.setAdapter(scaleAdapter);

        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        //changeRefreshingValue(true);
    }

}
