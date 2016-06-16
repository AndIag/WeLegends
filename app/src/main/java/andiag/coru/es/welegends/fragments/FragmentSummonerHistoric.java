package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.adapters.AdapterSummonerHistoric;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.rest.entities.Summoner;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSummonerHistoric extends Fragment {

    private final static String TAG = "FragmentSumHistoric";
    private AdapterSummonerHistoric adapter;
    private RecyclerView recyclerView;
    private ActivitySummoner parentActivity;
    private DBSummoner db;

    public FragmentSummonerHistoric() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parentActivity = (ActivitySummoner) context;
        this.db = ((ActivitySummoner) context).getDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_summoner_historic, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSummoners);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inicialize recycler adapter
        initAdapter();

        //Fill with data from database
        adapter.addData(db.selectSummoners());

        return v;
    }

    public void initAdapter() {
        adapter = new AdapterSummonerHistoric(getActivity(), R.layout.item_summoner_historic, new ArrayList<Summoner>());
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Summoner summoner = adapter.getItem(i);
                parentActivity.throwNewActivity(summoner, true);
            }
        });
        adapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                Summoner summoner = adapter.getItem(i);
                if (db.deleteSummoner(summoner))
                    adapter.remove(i);
                return false;
            }
        });
    }

}
