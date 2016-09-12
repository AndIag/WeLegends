package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.activities.ActivityNotifiable;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.adapters.AdapterSummonerHistoric;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSummonerHistoric extends Fragment implements FragmentNotifiable<Summoner> {

    private final static String TAG = "FragmentSumHistoric";
    private AdapterSummonerHistoric adapter = null;
    private RecyclerView recyclerView = null;

    private ActivityNotifiable<Summoner> activityNotifiable = null;
    private ActivitySummoner parentActivity;

    private DBSummoner db;
    private List<Summoner> summoners = null;

    ProgressBar progressBar;

    public FragmentSummonerHistoric() {
        // Required empty public constructor
    }

    @Override
    public void setActivityNotifiable(ActivityNotifiable<Summoner> activityNotifiable) {
        this.activityNotifiable = activityNotifiable;
    }

    @Override
    public void setProgressBarState(int viewState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parentActivity = (ActivitySummoner) context;
        this.db = ((ActivitySummoner) context).getDb();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        summoners = db.getSummoners();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Fill with data from database
        if (summoners == null) {
            summoners = db.getSummoners();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_summoner_historic, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSummoners);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        // Inicialize recycler adapter
        initAdapter();
        //TODO this shit dont want to work!! Init sumonnerhistory adapter
        adapter.setNewData(summoners);

        return v;
    }

    public void initAdapter() {
        adapter = new AdapterSummonerHistoric(R.layout.item_summoner_historic, null);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Summoner summoner = adapter.getItem(i);
                apiSearchSummonerId(Utils.cleanString(summoner.getName()), summoner.getRegion());
                parentActivity.launchNewActivity(summoner, ActivitySummoner.SUMMONER_HISTORIC_FRAGMENT);
            }
        });
    }

    private void apiSearchSummonerId(String summonerName, final String region) {
        Call<Summoner> call = RestClient.getWeLegendsData(summonerName)
                .getSummonerByName(region, summonerName);

        call.enqueue(new Callback<Summoner>() {
            @Override
            public void onResponse(Call<Summoner> call, Response<Summoner> response) {
                Log.d(TAG, "onClickFindSummoner: Found-" + response.body().getId());
                Summoner newSummoner = response.body();
                newSummoner.setRegion(region);
                db.addSummoner(newSummoner);
                if (activityNotifiable == null) {
                    Log.e(TAG, "callbackUpdateSummoner: Null noticeable activity. This should never happen");
                    return;
                }
                activityNotifiable.notifyDataChange(newSummoner);
            }

            @Override
            public void onFailure(Call<Summoner> call, Throwable t) {
                //This case may never happen
                Log.d(TAG, "onClickFindSummoner: Summoner not found");
            }
        });
    }

}
