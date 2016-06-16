package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        Log.d(TAG,"onCreateView - Start");
        View v = inflater.inflate(R.layout.fragment_summoner_historic, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSummoners);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterSummonerHistoric(getActivity(),R.layout.item_summoner_historic,new ArrayList<Summoner>());
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        recyclerView.setAdapter(adapter);

//        List<Summoner> summoners = new ArrayList<>();
//        Summoner s;
//        for(int i=0;i<10;i++){
//            s = new Summoner();
//            s.setName("Summoner "+i);
//            s.setRegion("EUW");
//            summoners.add(s);
//        }

        adapter.addData(db.selectSummoners());
        return v;
    }

}
