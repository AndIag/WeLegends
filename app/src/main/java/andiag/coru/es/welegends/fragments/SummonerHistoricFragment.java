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
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.activities.NotifiableActivity;
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
public class SummonerHistoricFragment extends Fragment implements NotifiableFragment<Summoner> {

    private final static String TAG = "FragmentSumHistoric";
    private AdapterSummonerHistoric adapter;
    private RecyclerView recyclerView;

    private NotifiableActivity<Summoner> notifiableActivity = null;
    private ActivitySummoner parentActivity;

    private DBSummoner db;

    public SummonerHistoricFragment() {
        // Required empty public constructor
    }

    public void setNotifiableActivity(NotifiableActivity<Summoner> notifiableActivity) {
        this.notifiableActivity = notifiableActivity;
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
        //TODO historic is not showed until u rotate the screen
        adapter.addData(db.getSummoners());

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
                apiSearchSummonerId(Utils.cleanString(summoner.getName()), summoner.getRegion());
                parentActivity.throwNewActivity(summoner, ActivitySummoner.SUMMONER_HISTORIC_FRAGMENT);
            }
        });
        adapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                //TODO argue if we really want this
                Summoner summoner = adapter.getItem(i);
                if (db.deleteSummoner(summoner))
                    adapter.remove(i);
                return false;
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
                if (notifiableActivity == null) {
                    Log.e(TAG, "callbackUpdateSummoner: Null noticeable activity. This should never happen");
                    return;
                }
                notifiableActivity.notifyDataChange(newSummoner);
            }

            @Override
            public void onFailure(Call<Summoner> call, Throwable t) {
                //This case may never happen
                Log.d(TAG, "onClickFindSummoner: Summoner not found");
            }
        });
    }

}
