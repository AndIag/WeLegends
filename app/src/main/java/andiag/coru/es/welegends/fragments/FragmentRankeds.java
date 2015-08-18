package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;
import andiag.coru.es.welegends.utils.static_data.NamesHandler;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Andy on 26/06/2015.
 */
public class FragmentRankeds extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    //AUXILIAR DATA
    private final int INCREMENT = 10;
    //VIEW DATA
    private ScaleInAnimationAdapter scaleAdapter;
    private ObservableRecyclerView recyclerView;
    private AdapterHistory recyclerAdapter;
    private GridLayoutManager layoutManager;
    //METRICS
    private DisplayMetrics outMetrics;
    private Display display;
    private int BEGININDEX;
    private int ENDINDEX;

    //BASIC DATA
    private String region;
    private long summoner_id;
    private ArrayList<Match> matchesHistoryList;
    //NEEDED METHODS
    private String request;

    public static FragmentRankeds newInstance(long summoner_id, String region) {
        FragmentRankeds fragmentRankeds = new FragmentRankeds();
        Bundle args = new Bundle();
        args.putLong("summoner_id", summoner_id);
        args.putString("region", region);
        fragmentRankeds.setArguments(args);
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
        if (getArguments() != null) {
            summoner_id = getArguments().getLong("summoner_id");
            region = getArguments().getString("region");
        }
        if (savedInstanceState != null) { //Load saved data in onPause
            BEGININDEX = savedInstanceState.getInt("beginIndex");
            ENDINDEX = savedInstanceState.getInt("endIndex");
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            matchesHistoryList = (ArrayList<Match>) savedInstanceState.getSerializable("matchesHistory");
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
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (matchesHistoryList == null) {
            startIndex();
            getSummonerHistory();
        } else {
            recyclerAdapter.clearHistory();
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
                //Load new values
                startIndex();
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
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                //When we reach our limit we load mote matches
                if (!isLoading()) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 1) {
                        getSummonerHistory();
                    }
                }
            }
        });

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

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        request = handler.getServer() + region.toLowerCase() + handler.getMatchHistory() + summoner_id + "/" + BEGININDEX + "/" + ENDINDEX;
        incrementIndexes();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray arrayMatches = null;
                        try {
                            arrayMatches = response.getJSONArray("matches");
                            if(arrayMatches!=null) {
                                new ParseDataTask(arrayMatches).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        } catch (JSONException e) {
                            if(matchesHistoryList==null || matchesHistoryList.size()<=0) {
                                activityMain.setUnranked();
                            }
                            changeRefreshingValue(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                decrementIndexes();
                if(isAdded()) {
                    Toast.makeText(activityMain, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                }
                changeRefreshingValue(false);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(activityMain).getRequestQueue().add(jsonObjectRequest);
    }

    private class ParseDataTask extends AsyncTask<Void, Void, ArrayList<Match>> {

        private final Gson gson = new Gson();
        private JSONArray array;

        public ParseDataTask(JSONArray array) {
            this.array = array;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeRefreshingValue(true);
        }

        @Override
        protected ArrayList<Match> doInBackground(Void... voids) {

            ArrayList<Match> list = new ArrayList<>();

            try {
                Match match;
                Object object;
                for (int i = 0; i < array.length(); i++) {
                    object = array.get(i);
                    match = gson.fromJson(object.toString(), Match.class);
                    list.add(match);
                }
                Collections.reverse(list);
            } catch (JSONException e) {
                e.printStackTrace();
                changeRefreshingValue(false);
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Match> matches) {
            super.onPostExecute(matches);
            if(matches!=null && matches.size()>0) {
                matchesHistoryList.addAll(matches);
                new RetrieveDataTask(matches).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private class RetrieveDataTask extends AsyncTask<Void, Void, ArrayList<Bundle>> {

        private ArrayList<Match> matches;
        private DateFormat dateF;

        public RetrieveDataTask(ArrayList<Match> m) {
            this.matches = m;
            dateF = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, activityMain.getResources().getConfiguration().locale);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeRefreshingValue(true);
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
            String d, date_s;
            Participant p;

            for (Match m : matches) {
                mapid = m.getMapId();
                creation = m.getMatchCreation();
                duration = m.getMatchDuration();

                p = m.getParticipants().get(0);
                champId = p.getChampionId();
                stats = p.getStats();

                kills = stats.getKills();
                deaths = stats.getDeaths();
                assists = stats.getAssists();
                lvl = stats.getChampLevel();
                minions = stats.getMinionsKilled();
                winner = stats.isWinner();
                gold = stats.getGoldEarned();

                d = String.format("%d' %d''",
                        TimeUnit.SECONDS.toMinutes(duration),
                        duration -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
                );

                date.setTimeInMillis(creation);
                date_s = dateF.format(date.getTime());

                data = new Bundle();
                data.putLong("matchId", m.getMatchId());
                data.putInt("champId", champId);
                data.putString("champName", ChampionsHandler.getChampName(champId));
                data.putString("champKey", ChampionsHandler.getChampKey(champId));
                data.putInt("mapId", mapid);
                data.putInt("mapName", NamesHandler.getMapName(mapid));
                data.putInt("mapImage", ImagesHandler.getMap(mapid));
                data.putLong("kills", kills);
                data.putLong("death", deaths);
                data.putLong("assist", assists);
                data.putLong("lvl", lvl);
                data.putLong("cs", minions);
                data.putFloat("gold", gold);
                data.putBoolean("winner", winner);
                data.putString("startDate", date_s);
                data.putString("duration", d);
                data.putInt("matchType", android.R.drawable.ic_menu_compass);
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
