package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.ArrayList;

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.AggregatedStatsDto;
import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.RankedStatsDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterRankedChamps;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentRankedChampStats extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    private ObservableRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    //METRICS
    private DisplayMetrics outMetrics;
    private Display display;
    private AdapterRankedChamps adapter;

    private RankedStatsDto rankedStatsDto;
    private String region;
    private long summoner_id;
    private String request;

    public static FragmentRankedChampStats newInstance(long summoner_id, String region) {
        FragmentRankedChampStats fragmentRankedChampStats = new FragmentRankedChampStats();
        Bundle args = new Bundle();
        args.putLong("summoner_id", summoner_id);
        args.putString("region", region);
        fragmentRankedChampStats.setArguments(args);
        return fragmentRankedChampStats;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("summoner_id", summoner_id);
        outState.putString("region", region);
        outState.putSerializable("rankedStatsDto", rankedStatsDto);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (getArguments() != null) {
            summoner_id = getArguments().getLong("summoner_id");
            region = getArguments().getString("region");
        }
        if (savedInstanceState != null) {
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            rankedStatsDto = (RankedStatsDto) savedInstanceState.getSerializable("rankedStatsDto");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityMain) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);
        adapter = new AdapterRankedChamps(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rankedStatsDto == null) {
            getChamps();
        } else {
            adapter.clearChamps();
            new RetrieveDataTask(rankedStatsDto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearChamps();
                getChamps();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranked_champions_stats, container, false);

        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.scroll);

        initializeRefresh(rootView);

        recyclerView.setHasFixedSize(true);

        display = activityMain.getWindowManager().getDefaultDisplay();
        if (outMetrics == null) {
            outMetrics = new DisplayMetrics();
        }
        display.getMetrics(outMetrics);

        float density = activityMain.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);

        layoutManager = new GridLayoutManager(activityMain, columns);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void getChamps() {
        if (isLoading()) return;

        final Gson gson = new Gson();
        changeRefreshingValue(true);

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        request = handler.getServer() + region + handler.getStats() + summoner_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        rankedStatsDto = gson.fromJson(response.toString(), RankedStatsDto.class);
                        new RetrieveDataTask(rankedStatsDto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String message = activityMain.getString(R.string.errorDefault);
                    switch (networkResponse.statusCode) {
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                            message = activityMain.getString(R.string.error500);
                            break;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE:
                            message = activityMain.getString(R.string.error503);
                            break;
                    }
                    Toast.makeText(activityMain, message
                            , Toast.LENGTH_LONG).show();

                    changeRefreshingValue(false);
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(activityMain).getRequestQueue().add(jsonObjectRequest);
    }

    private class RetrieveDataTask extends AsyncTask<Void, Void, ArrayList<Bundle>> {

        private RankedStatsDto rankedStatsDto;

        public RetrieveDataTask(RankedStatsDto rankedStatsDto) {
            this.rankedStatsDto = rankedStatsDto;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeRefreshingValue(true);
        }

        @Override
        protected ArrayList<Bundle> doInBackground(Void... voids) {
            ArrayList<Bundle> bundles = new ArrayList<>();
            Bundle m = new Bundle();

            AggregatedStatsDto aggregatedStatsDto = rankedStatsDto.getChampions().get(rankedStatsDto.getChampions().size() - 1).getStats();

            float wins = aggregatedStatsDto.getTotalSessionsWon();
            m.putString("victories", String.valueOf((int) wins));
            float lost = aggregatedStatsDto.getTotalSessionsLost();
            m.putString("defeats", String.valueOf((int) lost));
            int kills = aggregatedStatsDto.getTotalChampionKills();
            int death = aggregatedStatsDto.getTotalDeathsPerSession();
            int assist = aggregatedStatsDto.getTotalAssists();
            m.putString("globalkda", kills + "/" + death + "/" + assist);
            float percent = (wins / (wins + lost)) * 100;
            m.putString("percent", percent + "%");
            bundles.add(m);

            bundles.add(new Bundle());
            bundles.add(new Bundle());
            bundles.add(new Bundle());
            bundles.add(new Bundle());
            bundles.add(new Bundle());

            return bundles;
        }

        @Override
        protected void onPostExecute(ArrayList<Bundle> bundles) {
            super.onPostExecute(bundles);
            if (adapter != null) {
                adapter.updateChamps(bundles);
            }
            changeRefreshingValue(false);
        }
    }

}
