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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.google.gson.Gson;

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
import andiag.coru.es.welegends.utils.handlers.API;
import andiag.coru.es.welegends.utils.handlers.Images;
import andiag.coru.es.welegends.utils.handlers.MyNetworkError;
import andiag.coru.es.welegends.utils.handlers.Names;
import andiag.coru.es.welegends.utils.handlers.champions.Champions;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
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
    private AdapterHistory recyclerAdapter;
    //METRICS
    private DisplayMetrics outMetrics;
    private Display display;
    //ARGUMENTS
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

        if (recyclerAdapter == null) {
            recyclerAdapter = new AdapterHistory(activityMain);
            scaleAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
            scaleAdapter.setFirstOnly(false);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (recentGames == null) {
            getSummonerHistory();
        } else {
            recyclerAdapter.clearHistory();
            new RetrieveDataTask(recentGames).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
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
                //Load new values
                getSummonerHistory();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_history, container, false);

        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);

        initializeRefresh(view);

        recyclerView.setHasFixedSize(false);

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

        recyclerView.setAdapter(scaleAdapter);

        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        if (activityMain instanceof ObservableScrollViewCallbacks) {
            recyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) activityMain);
        }

        return view;
    }

    private void getSummonerHistory() {
        if (isLoading()) return;

        changeRefreshingValue(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.getHistory(region, summoner_id), (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        recentGames = gson.fromJson(response.toString(), RecentGamesDto.class);
                        new RetrieveDataTask(recentGames).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

        private RecentGamesDto recentGames;
        private DateFormat dateF;

        public RetrieveDataTask(RecentGamesDto recentGames) {
            this.recentGames = recentGames;
            dateF = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, activityMain.getResources().getConfiguration().locale);
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

                d = String.format("%d' %d''",
                        TimeUnit.SECONDS.toMinutes(duration),
                        duration -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
                );

                date.setTimeInMillis(creation);
                date_s = dateF.format(date.getTime());

                data = new Bundle();
                data.putLong("matchId", g.getGameId());
                data.putInt("champId", champId);
                data.putString("champName", Champions.getChampName(champId));
                data.putString("champKey", Champions.getChampKey(champId));
                data.putInt("mapId", mapid);
                data.putInt("mapName", Names.getMapName(mapid));
                data.putInt("mapImage", Images.getMap(mapid));
                data.putLong("kills", kills);
                data.putLong("death", deaths);
                data.putLong("assist", assists);
                data.putLong("lvl", lvl);
                data.putLong("cs", minions);
                data.putFloat("gold", gold);
                data.putBoolean("winner", winner);
                data.putString("startDate", date_s);
                data.putString("duration", d);
                if (g.getSubType().contains("RANKED") && !(g.getSubType().contains("UNRANKED"))) {
                    data.putInt("matchType", android.R.drawable.ic_menu_compass);
                }
                if (g.getSubType().contains("BOT")) {
                    data.putInt("matchType", R.drawable.bot);
                }
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
            }
            changeRefreshingValue(false);
        }
    }

}
