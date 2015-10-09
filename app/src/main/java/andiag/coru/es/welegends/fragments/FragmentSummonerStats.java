package andiag.coru.es.welegends.fragments;

import android.app.Activity;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.DTOs.currentGame.CurrentGameInfo;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterPlayerStats;
import andiag.coru.es.welegends.entities.Entry;
import andiag.coru.es.welegends.entities.League;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.utils.API;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.Names;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentSummonerStats extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    //NEEDED METHODS
    private final Gson gson = new Gson();
    private View rootView;
    private ImageLoader imageLoader;
    private AdapterPlayerStats adapter;
    private ScaleInAnimationAdapter scaleAdapter;
    private ObservableRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    //ARGUMENTS
    private Summoner summoner;
    private String region;

    private ArrayList<League> leagues = new ArrayList<>();
    private boolean isPlaying = false;

    public FragmentSummonerStats() {
        imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
    }

    public static FragmentSummonerStats newInstance(String region, Summoner summoner) {
        FragmentSummonerStats fragmentSummonerStats = new FragmentSummonerStats();
        Bundle args = new Bundle();
        args.putSerializable("summoner", summoner);
        args.putString("region", region);
        fragmentSummonerStats.setArguments(args);
        return fragmentSummonerStats;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        if (summoner.getSummonerLevel() == 30 && !activityMain.isUnranked()) {
            setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    leagues.clear();
                    getLeagues();
                }
            });
        } else {
            setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    changeRefreshingValue(false);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("region", region);
        outState.putSerializable("summoner", summoner);
        outState.putSerializable("leagues", leagues);
        outState.putBoolean("isPlaying", isPlaying);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        //Se ejecuta al girar la pantalla no al cambiar de tab
        if (getArguments() != null) {
            summoner = (Summoner) getArguments().getSerializable("summoner");
            region = getArguments().getString("region");
        }
        if (savedInstanceState != null) { //Load saved data in onPause
            leagues = (ArrayList<League>) savedInstanceState.getSerializable("leagues");
            summoner = (Summoner) savedInstanceState.getSerializable("summoner");
            region = savedInstanceState.getString("region");
            isPlaying = savedInstanceState.getBoolean("isPlaying", false);
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
        adapter = new AdapterPlayerStats(activityMain);
        scaleAdapter = new ScaleInAnimationAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imageLoader == null) {
            imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
        }
        isPlaying();
        if (summoner.getSummonerLevel() == 30 && !activityMain.isUnranked()) {
            if (leagues.isEmpty()) {
                getLeagues();
            } else setInfoInView();
        } else {
            setNotLVL30Info();
        }
    }

    @Override //Si se ejecuta al cambiar 2 fragments para el lado
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_summoner_stats, container, false);

        initializeRefresh(rootView);

        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.scroll);

        layoutManager = new LinearLayoutManager(activityMain);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(scaleAdapter);

        recyclerView.setTouchInterceptionViewGroup((ViewGroup) activityMain.findViewById(R.id.container));

        return rootView;
    }

    private void setNotLVL30Info() {
        ArrayList<Bundle> stats = new ArrayList<>();
        if (getProfileBundle() == null) {
            adapter.updateStats(stats);
            scaleAdapter.notifyDataSetChanged();
            return;
        }
        stats.add(getProfileBundle());

        League l = new League();
        Entry e = new Entry();
        e.setDivision("");
        e.setWins(0);
        e.setLosses(0);
        e.setPlayerOrTeamName(summoner.getName());
        e.setLeaguePoints(0);
        l.setName(summoner.getName());
        l.setTier("Unranked");
        List list = new ArrayList<Entry>();
        list.add(e);
        l.setEntries(list);

        stats.add(getDividerBundle("Solo"));
        stats.add(getItemBundle(l));

        adapter.updateStats(stats);
        scaleAdapter.notifyDataSetChanged();
    }

    private void setInfoInView() {
        ArrayList<Bundle> stats = new ArrayList<>();
        if (getProfileBundle() == null) {
            adapter.updateStats(stats);
            scaleAdapter.notifyDataSetChanged();
            return;
        }

        stats.add(getProfileBundle());
        stats.add(getStatusBundle("Not playing"));

        int pos5 = -1, pos3 = -1;

        Bundle index;
        for (League l : leagues) {
            switch (l.getQueue()) {
                case "RANKED_SOLO_5x5":

                    stats.add(getDividerBundle("Solo"));

                    stats.add(getItemBundle(l));
                    break;
                case "RANKED_TEAM_3x3":
                    if (pos3 < 0) {
                        index = getDividerBundle("Teams 3vs3");
                        stats.add(index);
                        pos3 = stats.indexOf(index);
                    }
                    if ((pos3 + 1) <= pos5) pos5++;

                    stats.add(pos3 + 1, getItemBundle(l));
                    break;
                case "RANKED_TEAM_5x5":
                    if (pos5 < 0) {
                        index = getDividerBundle("Teams 5vs5");
                        stats.add(index);
                        pos5 = stats.indexOf(index);
                    }
                    if ((pos5 + 1) <= pos3) pos3++;

                    stats.add(pos5 + 1, getItemBundle(l));
                    break;
            }
        }

        adapter.updateStats(stats);
        scaleAdapter.notifyDataSetChanged();
        scaleAdapter.setFirstOnly(false);
    }

    private Bundle getDividerBundle(String title) {
        Bundle b = new Bundle();
        b.putString("divider", title);
        b.putInt("type", AdapterPlayerStats.TYPE_DIVIDER);
        return b;
    }

    private Bundle getStatusBundle(String title) {
        Bundle b = new Bundle();
        b.putString("divider", title);
        b.putInt("type", AdapterPlayerStats.TYPE_STATUS);
        return b;
    }

    private Bundle getProfileBundle() {
        if (!isAdded()) return null;
        Bundle b = new Bundle();
        b.putString("level", getString(R.string.level) + " " + summoner.getSummonerLevel());
        b.putString("summoner", summoner.getName());
        b.putString("server", region.toUpperCase());
        b.putString("url", API.getProfileIcon(summoner.getProfileIconId()));
        b.putInt("type", AdapterPlayerStats.TYPE_HEADER);
        return b;
    }

    private Bundle getItemBundle(League l) {

        Bundle item = new Bundle();
        Entry entry = l.getEntries().get(0);
        item.putString("division", l.getTier() + " " + entry.getDivision());
        item.putString("divname", l.getName());
        item.putString("wins", Integer.toString(entry.getWins()));
        item.putString("losses", Integer.toString(entry.getLosses()));
        item.putString("lp", Integer.toString(entry.getLeaguePoints()));
        item.putString("name", entry.getPlayerOrTeamName());
        item.putInt("type", AdapterPlayerStats.TYPE_ITEM);

        String imres = l.getTier() + entry.getDivision();
        int id = activityMain.getResources().getIdentifier(imres.toLowerCase(), "drawable", activityMain.getPackageName());
        item.putInt("image", id);

        return item;

    }

    private void getLeagues() {
        if (summoner.getSummonerLevel() != 30) return;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.getLeagues(region, summoner.getId())
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray arrayLeagues = null;
                        try {
                            arrayLeagues = response.getJSONArray(Long.toString(summoner.getId()));
                            League l;
                            for (int i = 0; i < arrayLeagues.length(); i++) {
                                l = gson.fromJson(arrayLeagues.get(i).toString(), League.class);
                                leagues.add(l);
                            }
                            setInfoInView();
                            changeRefreshingValue(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            changeRefreshingValue(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    if (networkResponse.statusCode == HttpStatus.SC_NOT_FOUND) {
                        setNotLVL30Info();
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

    private void isPlaying() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , API.getCurrent(region, summoner.getId(), Names.getPlatformId(region)), (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        isPlaying = true;
                        activityMain.setCurrentGameInfo(gson.fromJson(response.toString(), CurrentGameInfo.class));
                        adapter.updatePlayerStatus(isPlaying);
                        scaleAdapter.notifyItemChanged(1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    if (networkResponse.statusCode == HttpStatus.SC_NOT_FOUND) {
                        isPlaying = false;
                        return;
                    }
                }
                if (isAdded()) {
                    Toast.makeText(activityMain, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(activityMain).getRequestQueue().add(jsonObjectRequest);
    }

}
