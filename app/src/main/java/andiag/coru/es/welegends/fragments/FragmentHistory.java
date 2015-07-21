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
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import andiag.coru.es.welegends.DTOs.recentGamesDTOs.GameDto;
import andiag.coru.es.welegends.DTOs.recentGamesDTOs.RecentGamesDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterHistory;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;
import andiag.coru.es.welegends.utils.static_data.NamesHandler;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Iago on 13/07/2015.
 */
public class FragmentHistory extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;

    private final Gson gson = new Gson();
    private ObservableRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private ScaleInAnimationAdapter scaleAdapter;
    private AlphaInAnimationAdapter alphaAdapter;
    private AdapterHistory recyclerAdapter;
    //METRICS
    private DisplayMetrics outMetrics;
    private Display display;
    private long summoner_id;
    private String region;
    private RecentGamesDto recentGames;
    private String request;

    public static FragmentHistory newInstance(long summoner_id, String region) {
        FragmentHistory fragmentHistory = new FragmentHistory();
        Bundle args = new Bundle();
        args.putLong("summoner_id", summoner_id);
        args.putString("region", region);
        fragmentHistory.setArguments(args);
        return fragmentHistory;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear our adapter
                recyclerAdapter.clearHistory();
                scaleAdapter.notifyDataSetChanged();
                alphaAdapter.notifyDataSetChanged();
                //Load new values
                getSummonerHistory();
            }
        });
    }

    //SAVE AND RETRIEVE DATA
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("summoner_id", summoner_id);
        outState.putString("region", region);
        outState.putSerializable("recentgames", recentGames);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (getArguments() != null) {
            summoner_id = getArguments().getLong("summoner_id");
            region = getArguments().getString("region");
        }
        if (savedInstanceState != null) {
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            recentGames = (RecentGamesDto) savedInstanceState.getSerializable("recentgames");
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recentGames == null) {
            getSummonerHistory();
        } else {
            new RetrieveDataTask(recentGames).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);

        initializeRefresh(view);

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

        if (recyclerAdapter == null) {
            recyclerAdapter = new AdapterHistory(activityMain, summoner_id);
            scaleAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
            alphaAdapter = new AlphaInAnimationAdapter(scaleAdapter);
            alphaAdapter.setFirstOnly(false);
        }

        recyclerView.setAdapter(alphaAdapter);

        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        if (activityMain instanceof ObservableScrollViewCallbacks) {
            recyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) activityMain);
        }

        return view;
    }

    private void getSummonerHistory() {
        if (isLoading()) return;

        changeRefreshingValue(true);

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        request = handler.getServer() + handler.getRecent_games() + summoner_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recentGames = gson.fromJson(response.toString(), RecentGamesDto.class);
                        new RetrieveDataTask(recentGames).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        private RecentGamesDto recentGames;
        private DateFormat dateF;

        public RetrieveDataTask(RecentGamesDto recentGames) {
            this.recentGames = recentGames;
            dateF = DateFormat.getDateInstance(DateFormat.SHORT, activityMain.getResources().getConfiguration().locale);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeRefreshingValue(true);
        }

        @Override
        protected ArrayList<Bundle> doInBackground(Void... voids) {
            int mapid, champId;
            long creation, duration;
            long kills, assists, deaths, minions, lvl, gold;
            boolean winner;
            Calendar date = Calendar.getInstance();
            Bundle data;
            ArrayList<Bundle> bundles = new ArrayList<>();
            String d, date_s;

            for (GameDto g : recentGames.getGames()) {
                champId = g.getChampionId();
                mapid = g.getMapId();
                lvl = g.getStats().getLevel();
                winner = g.getStats().getWin();
                kills = g.getStats().getChampionsKilled();
                deaths = g.getStats().getNumDeaths();
                assists = g.getStats().getAssists();
                minions = g.getStats().getMinionsKilled() + g.getStats().getNeutralMinionsKilled();
                gold = g.getStats().getGoldEarned();
                creation = g.getCreateDate();
                duration = g.getStats().getTimePlayed();

                d = String.format("%d ' %d ''",
                        TimeUnit.SECONDS.toMinutes(duration),
                        duration -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
                );

                date.setTimeInMillis(creation);
                date_s = dateF.format(date.getTime());

                data = new Bundle();
                data.putLong("matchId", g.getGameId());
                data.putString("champName", NamesHandler.getChampName(champId));
                data.putInt("champImage", ImagesHandler.getChamp(champId));
                data.putInt("mapName", NamesHandler.getMapName(mapid));
                data.putInt("mapImage", ImagesHandler.getMap(mapid));
                data.putString("kda", kills + "/" + deaths + "/" + assists);
                data.putString("lvl", Long.toString(lvl));
                data.putString("cs", Long.toString(minions));
                data.putString("gold", String.format("%.1f", (float) gold / 1000) + "k");
                data.putBoolean("winner", winner);
                data.putString("duration", date_s + "   " + d);
                bundles.add(data);
            }
            return bundles;
        }

        @Override
        protected void onPostExecute(ArrayList<Bundle> bundles) {
            super.onPostExecute(bundles);
            if (recyclerAdapter != null) {
                recyclerAdapter.updateHistory(bundles);
                scaleAdapter.notifyDataSetChanged();
                alphaAdapter.notifyDataSetChanged();
            }
            changeRefreshingValue(false);
        }
    }

}
