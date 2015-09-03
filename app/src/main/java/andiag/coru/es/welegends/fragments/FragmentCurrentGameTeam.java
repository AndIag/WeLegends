package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityChampionStatsDetails;
import andiag.coru.es.welegends.adapters.AdapterCurrentGameTeams;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by iagoc on 03/09/2015.
 */
public class FragmentCurrentGameTeam extends Fragment {

    private static ActivityChampionStatsDetails activityMain;

    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterCurrentGameTeams adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    private boolean isWinner;

    public static FragmentVictoryDefeatDetails newInstance(boolean type) {
        FragmentVictoryDefeatDetails f = new FragmentVictoryDefeatDetails();
        Bundle arguments = new Bundle();
        arguments.putBoolean("isWinner", type);
        f.setArguments(arguments);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityChampionStatsDetails) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isWinner = getArguments().getBoolean("isWinner");
        if (adapter == null) {
            adapter = new AdapterCurrentGameTeams(activityMain);
            scaleAdapter = new ScaleInAnimationAdapter(adapter);
            scaleAdapter.setFirstOnly(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override //Si se ejecuta al cambiar 2 fragments para el lado
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_champions_stats, container, false);

        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.scroll);
        layoutManager = new LinearLayoutManager(activityMain);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(scaleAdapter);
        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        return rootView;
    }
}
