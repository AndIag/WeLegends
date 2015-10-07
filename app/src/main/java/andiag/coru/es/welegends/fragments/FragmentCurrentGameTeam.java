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
import andiag.coru.es.welegends.activities.ActivityCurrentGameInfo;
import andiag.coru.es.welegends.adapters.AdapterCurrentGameTeams;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by iagoc on 03/09/2015.
 */
public class FragmentCurrentGameTeam extends Fragment {

    private static ActivityCurrentGameInfo activityMain;

    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterCurrentGameTeams adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    private boolean myTab;

    public static FragmentCurrentGameTeam newInstance(boolean type) {
        FragmentCurrentGameTeam f = new FragmentCurrentGameTeam();
        Bundle arguments = new Bundle();
        arguments.putBoolean("myTab", type);
        f.setArguments(arguments);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityCurrentGameInfo) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTab = getArguments().getBoolean("myTab");
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
