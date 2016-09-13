package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSummonerHistoric extends FragmentBase implements FragmentNotifiable<Summoner> {

    private final static String TAG = "FragmentSumHistoric";

    @BindView(R.id.recyclerSummoners)
    RecyclerView recycler;

    private AdapterSummonerHistoric adapter = null;

    private ActivityNotifiable<Summoner> activityNotifiable = null;

    private DBSummoner db;
    private List<Summoner> summoners = null;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(parentActivity));
        initAdapter();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_summoner_historic;
    }

    public void initAdapter() {
        adapter = new AdapterSummonerHistoric(R.layout.item_summoner_historic, null);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Summoner summoner = adapter.getItem(i);
                apiSearchSummonerId(Utils.cleanString(summoner.getName()), summoner.getRegion());
                ((ActivitySummoner)parentActivity).launchNewActivity(summoner, ActivitySummoner.SUMMONER_HISTORIC_FRAGMENT);
            }
        });
        adapter.setNewData(summoners);
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
