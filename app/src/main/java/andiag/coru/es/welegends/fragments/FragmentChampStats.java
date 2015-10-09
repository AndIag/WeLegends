package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.AggregatedStatsDto;
import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.ChampionStatsDto;
import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.RankedStatsDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterChampStats;
import andiag.coru.es.welegends.utils.API;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.Champions;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentChampStats extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //METRICS
    private AdapterChampStats adapter;
    private ScaleInAnimationAdapter scaleAdapter;

    private RankedStatsDto rankedStatsDto;
    private String region;
    private long summoner_id;
    private String request;

    public static FragmentChampStats newInstance(long summoner_id, String region) {
        FragmentChampStats fragmentChampStats = new FragmentChampStats();
        Bundle args = new Bundle();
        args.putLong("summoner_id", summoner_id);
        args.putString("region", region);
        fragmentChampStats.setArguments(args);
        return fragmentChampStats;
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
        if (adapter == null) {
            adapter = new AdapterChampStats(activityMain);
            scaleAdapter = new ScaleInAnimationAdapter(adapter);
            scaleAdapter.setFirstOnly(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rankedStatsDto == null) {
            getChamps();
        } else {
            if (adapter.needReload()) {
                adapter.clearChamps();
                scaleAdapter.notifyDataSetChanged();
                new RetrieveDataTask(rankedStatsDto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearChamps();
                scaleAdapter.notifyDataSetChanged();
                getChamps();
            }
        });
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

    private void getChamps() {
        if (isLoading()) return;
        final Gson gson = new Gson();
        changeRefreshingValue(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.getStats(region, summoner_id), (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        rankedStatsDto = gson.fromJson(response.toString(), RankedStatsDto.class);
                        if (rankedStatsDto != null) {
                            new RetrieveDataTask(rankedStatsDto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("RankedChampStats", "Error: " + error.getMessage());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    if (networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        activityMain.setUnranked();
                        changeRefreshingValue(false);
                        return;
                    }
                }
                if (isAdded()) {
                    Toast.makeText(activityMain, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                }
                changeRefreshingValue(false);
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

        private void sortChampionStatsDto(ArrayList<ChampionStatsDto> championStats) {
            Collections.sort(championStats, new Comparator<ChampionStatsDto>() {
                @Override
                public int compare(ChampionStatsDto lhs, ChampionStatsDto rhs) {
                    int lTotalGames = lhs.getStats().getTotalSessionsPlayed();
                    int rTotalGames = rhs.getStats().getTotalSessionsPlayed();
                    if (lTotalGames < rTotalGames) {
                        return 1;
                    }
                    if (lTotalGames > rTotalGames) {
                        return -1;
                    }
                    return 0;
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeRefreshingValue(true);
        }

        @Override
        protected ArrayList<Bundle> doInBackground(Void... voids) {
            ArrayList<Bundle> bundles = new ArrayList<>();
            ArrayList<ChampionStatsDto> championStats = rankedStatsDto.getChampions();
            AggregatedStatsDto aggregatedStatsDto;
            Bundle m;
            Bundle summonerBundle = new Bundle();
            float wins, lost, totalGames;
            int maxUsedChampId = 0, maxGamesPlayed = 0;
            int id;

            sortChampionStatsDto(championStats);

            for (ChampionStatsDto c : championStats) {
                m = new Bundle();
                id = c.getId();
                aggregatedStatsDto = c.getStats();

                wins = aggregatedStatsDto.getTotalSessionsWon();
                lost = aggregatedStatsDto.getTotalSessionsLost();
                totalGames = wins + lost;

                m.putFloat("victories", wins);
                m.putFloat("defeats", lost);
                m.putFloat("totalGames", totalGames);
                m.putFloat("kills", aggregatedStatsDto.getTotalChampionKills());
                m.putFloat("death", aggregatedStatsDto.getTotalDeathsPerSession());
                m.putFloat("assist", aggregatedStatsDto.getTotalAssists());
                m.putFloat("cs", aggregatedStatsDto.getTotalMinionKills() + aggregatedStatsDto.getTotalNeutralMinionsKilled());
                m.putFloat("gold", aggregatedStatsDto.getTotalGoldEarned());
                m.putInt("penta", aggregatedStatsDto.getTotalPentaKills());
                m.putInt("quadra", aggregatedStatsDto.getTotalQuadraKills());
                m.putInt("triple", aggregatedStatsDto.getTotalTripleKills());
                m.putInt("double", aggregatedStatsDto.getTotalDoubleKills());
                m.putInt("turrets", aggregatedStatsDto.getTotalTurretsKilled());
                m.putInt("dealt", aggregatedStatsDto.getTotalDamageDealt());
                m.putInt("taken", aggregatedStatsDto.getTotalDamageTaken());


                if (id == 0) { //Summoner Data
                    summonerBundle = m;
                    summonerBundle.putString("summonerName", activityMain.getSummoner().getName());
                    summonerBundle.putLong("summonerProfileId", activityMain.getSummoner().getProfileIconId());
                } else {
                    m.putInt("champId", id);
                    m.putString("key", Champions.getChampKey(id));
                    m.putString("name", Champions.getChampName(id));
                    bundles.add(m);

                    if ((totalGames) > maxGamesPlayed) {
                        maxGamesPlayed = (int) totalGames;
                        maxUsedChampId = id;
                    }
                }
            }

            summonerBundle.putInt("champId", maxUsedChampId);
            summonerBundle.putString("key", Champions.getChampKey(maxUsedChampId));
            summonerBundle.putString("name", Champions.getChampName(maxUsedChampId));
            bundles.add(0, summonerBundle);
            return bundles;
        }

        @Override
        protected void onPostExecute(ArrayList<Bundle> bundles) {
            super.onPostExecute(bundles);
            if (adapter != null) {
                adapter.updateChamps(bundles);
                scaleAdapter.notifyDataSetChanged();
            }
            changeRefreshingValue(false);
        }
    }

}
