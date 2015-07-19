package andiag.coru.es.welegends.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterHistory;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantIdentities;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;
import andiag.coru.es.welegends.utils.static_data.NamesHandler;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Andy on 26/06/2015.
 */
public class FragmentRankeds extends SwipeRefreshLayoutFragment {

    private static FragmentRankeds fragmentRankeds;
    private static ActivityMain activityMain;
    //AUXILIAR DATA
    private final int INCREMENT = 10;
    //VIEW DATA
    private ScaleInAnimationAdapter scaleAdapter;
    private AlphaInAnimationAdapter alphaAdapter;
    private ObservableRecyclerView recyclerView;
    private AdapterHistory recyclerAdapter;
    private GridLayoutManager layoutManager;
    private int BEGININDEX = 0;
    private int ENDINDEX = 10;
    private boolean isLoading = false;

    //BASIC DATA
    private String region;
    private long summoner_id;
    private ArrayList<Match> matchesHistoryList = new ArrayList<>();

    //CONSTRUCTION AND DESTRUCTION
    public FragmentRankeds() {
    }

    public static void deleteFragment() {
        fragmentRankeds = null;
    }

    public static FragmentRankeds getInstance(ActivityMain aM) {
        activityMain = aM;
        if (fragmentRankeds != null) {
            return fragmentRankeds;
        }
        fragmentRankeds = new FragmentRankeds();
        return fragmentRankeds;
    }

    //GETTERS, SETTERS && ADDS
    private void incrementIndexes() {
        BEGININDEX += INCREMENT;
        ENDINDEX += INCREMENT;
    }

    private void decrementIndexes() {
        if (BEGININDEX - INCREMENT >= 0) {
            BEGININDEX -= INCREMENT;
            ENDINDEX -= INCREMENT;
        }
    }

    private void startIndex() {
        BEGININDEX = 0;
        ENDINDEX = INCREMENT;
        matchesHistoryList = new ArrayList<>();
    }

    public void setSummoner_id(long summoner_id, String region) {
        if (matchesHistoryList != null && matchesHistoryList.size() > 0) {
            return;
        }
        this.summoner_id = summoner_id;
        this.region = region;
        getSummonerHistory(BEGININDEX, ENDINDEX);
    }

    //SAVE AND RETRIEVE DATA
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("beginIndex", BEGININDEX);
        outState.putInt("endIndex", ENDINDEX);
        outState.putLong("summoner_id", summoner_id);
        outState.putString("region", region);
        outState.putSerializable("matchesHistory", matchesHistoryList);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        //Se ejecuta al girar la pantalla no al cambiar de tab
        if (savedInstanceState != null) { //Load saved data in onPause
            BEGININDEX = savedInstanceState.getInt("beginIndex");
            ENDINDEX = savedInstanceState.getInt("endIndex");
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            matchesHistoryList = (ArrayList<Match>) savedInstanceState.getSerializable("matchesHistory");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((matchesHistoryList != null) && (matchesHistoryList.size() > 0)) {
            new RetrieveDataTask(matchesHistoryList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                alphaAdapter.notifyDataSetChanged();
                //Load new values
                startIndex();
                getSummonerHistory(BEGININDEX, ENDINDEX);
            }
        });
        changeRefreshingValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);

        initializeRefresh(view);
        onRetrieveInstanceState(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        Display display = activityMain.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activityMain.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);

        layoutManager = new GridLayoutManager(activityMain, columns);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                //When we reach our limit we load mote matches
                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 1) {
                        getSummonerHistory(BEGININDEX, ENDINDEX);
                    }
                }
            }
        });

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

    //NEEDED METHODS
    private void getSummonerHistory(int beginIndex, int endIndex) {
        if (isLoading) return;

        isLoading = true;

        changeRefreshingValue(true);
        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        incrementIndexes();

        String request = handler.getServer() + region.toLowerCase() + handler.getMatchHistory() + summoner_id + "/" + beginIndex + "/" + endIndex;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray arrayMatches = null;
                        try {
                            arrayMatches = response.getJSONArray("matches");
                            new ParseDataTask(arrayMatches).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isLoading = false;
                            changeRefreshingValue(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                isLoading = false;
                changeRefreshingValue(false);
                decrementIndexes();
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
                }
            }
        });

        VolleyHelper.getInstance(activityMain).getRequestQueue().add(jsonObjectRequest);
    }

    private class ParseDataTask extends AsyncTask<Void, Void, ArrayList<Match>> {

        private final Gson gson = new Gson();
        private JSONArray array;

        public ParseDataTask(JSONArray array) {
            this.array = array;
        }

        @Override
        protected ArrayList<Match> doInBackground(Void... voids) {

            ArrayList<Match> list = new ArrayList<>();

            try {

                Match match;
                for (int i = 0; i < array.length(); i++) {
                    match = gson.fromJson(array.get(i).toString(), Match.class);
                    list.add(match);
                }
                Collections.reverse(list);
            } catch (JSONException e) {
                e.printStackTrace();
                isLoading = false;
                changeRefreshingValue(false);
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Match> matches) {
            super.onPostExecute(matches);
            matchesHistoryList.addAll(matches);
            new RetrieveDataTask(matches).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class RetrieveDataTask extends AsyncTask<Void, Void, ArrayList<Bundle>> {

        private ArrayList<Match> matches;
        private DateFormat dateF;

        public RetrieveDataTask(ArrayList<Match> m) {
            this.matches = m;
            dateF = DateFormat.getDateInstance(DateFormat.SHORT, activityMain.getResources().getConfiguration().locale);
        }

        @Override
        protected ArrayList<Bundle> doInBackground(Void... voids) {
            int mapid = 11, champId = 0;
            long creation = 0, duration = 0;
            long kills = 0, assists = 0, deaths = 0, minions = 0, lvl = 0, gold = 0;
            boolean winner = false;
            ParticipantStats stats;
            Calendar date = Calendar.getInstance();
            Bundle data;
            ArrayList<Bundle> bundles = new ArrayList<>();

            for (Match m : matches) {
                mapid = m.getMapId();
                creation = m.getMatchCreation();
                duration = m.getMatchDuration();

                long participantId = -1;
                for (ParticipantIdentities pi : m.getParticipantIdentities()) {
                    if (pi.getPlayer().getSummonerId() == summoner_id) {
                        participantId = pi.getParticipantId();
                        break;
                    }
                }

                for (Participant p : m.getParticipants()) {
                    if (p.getParticipantId() == participantId) {
                        stats = p.getStats();
                        champId = p.getChampionId();
                        kills = stats.getKills();
                        deaths = stats.getDeaths();
                        assists = stats.getAssists();
                        lvl = stats.getChampLevel();
                        minions = stats.getMinionsKilled();
                        winner = stats.isWinner();
                        gold = stats.getGoldEarned();
                        break;
                    }
                }

                String d = String.format("%d ' %d ''",
                        TimeUnit.SECONDS.toMinutes(duration),
                        duration -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
                );

                date.setTimeInMillis(creation);
                String date_s = dateF.format(date.getTime());

                data = new Bundle();
                data.putLong("matchId", m.getMatchId());
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
            recyclerAdapter.updateHistory(bundles);
            scaleAdapter.notifyDataSetChanged();
            alphaAdapter.notifyDataSetChanged();
            changeRefreshingValue(false);
            isLoading = false;
        }
    }
}
