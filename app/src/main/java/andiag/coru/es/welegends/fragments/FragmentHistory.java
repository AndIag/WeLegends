package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Collections;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.adapters.AdapterHistory;
import andiag.coru.es.welegends.adapters.ScaleInAnimationAdapter;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Andy on 26/06/2015.
 */
public class FragmentHistory extends Fragment {

    private final int INCREMENT = 10;
    ScaleInAnimationAdapter scaleAdapter;
    private ObservableRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private AdapterHistory recyclerAdapter;
    private GridLayoutManager layoutManager;
    private int BEGININDEX;
    private int ENDINDEX;

    private String region;
    private long summoner_id;
    private ArrayList<Match> matchesHistoryList;

    public FragmentHistory() {
    }

    public static FragmentHistory newInstance(String region,long id) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        args.putString("region",region);
        args.putLong("id",id);
        fragment.setArguments(args);
        return fragment;
    }

    //GETTERS, SETTERS && ADDS
    private void incrementIndexes() {
        BEGININDEX += INCREMENT;
        ENDINDEX += INCREMENT;
    }

    private void startIndex() {
        BEGININDEX = 0;
        ENDINDEX = INCREMENT;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("beginIndex", BEGININDEX);
        outState.putInt("endIndex", ENDINDEX);
        outState.putLong("summoner_id", summoner_id);
        outState.putString("region", region);
        outState.putSerializable("matchesHistory", matchesHistoryList);
    }

    //Used to activate the refreshing indicator
    public void changeRefreshingValue(boolean bool) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(bool);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            BEGININDEX = savedInstanceState.getInt("beginIndex");
            ENDINDEX = savedInstanceState.getInt("endIndex");
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            matchesHistoryList = (ArrayList<Match>) savedInstanceState.getSerializable("matchesHistory");
            recyclerAdapter.updateHistory(matchesHistoryList);
        } else if (getArguments() != null) {
            region = getArguments().getString("region");
            summoner_id = getArguments().getLong("id");
            startIndex();
            matchesHistoryList = new ArrayList<>();
            getSummonerHistory(BEGININDEX, ENDINDEX);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            BEGININDEX = savedInstanceState.getInt("beginIndex");
            ENDINDEX = savedInstanceState.getInt("endIndex");
            summoner_id = savedInstanceState.getLong("summoner_id");
            region = savedInstanceState.getString("region");
            matchesHistoryList = (ArrayList<Match>) savedInstanceState.getSerializable("matchesHistory");
            recyclerAdapter.updateHistory(matchesHistoryList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        Activity parentActivity = getActivity();
        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Clear our adapter
                        recyclerAdapter.clearHistory();
                        scaleAdapter.notifyDataSetChanged();
                        //Clear Data
                        matchesHistoryList = new ArrayList<>();
                        //Load new values
                        startIndex();
                        getSummonerHistory(BEGININDEX,ENDINDEX);
                    }
                }
        );
        recyclerView.setHasFixedSize(true);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 300);

        layoutManager = new GridLayoutManager(getActivity(), columns);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new AdapterHistory(getActivity(), summoner_id);
        scaleAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
        scaleAdapter.setFirstOnly(false);

        //scaleAdapter.setDuration(1000);
        recyclerView.setAdapter(scaleAdapter);


        recyclerView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            recyclerView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    private void getSummonerHistory(int beginIndex, int endIndex) {
        changeRefreshingValue(true);
        APIHandler handler = APIHandler.getInstance(getActivity());

        incrementIndexes();

        String request = "https://" + region + handler.getServer() + region
                + handler.getMatchHistory() + summoner_id + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&api_key=" + handler.getKey();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA", response.toString());
                        JSONArray arrayMatches = null;
                        try {
                            arrayMatches = response.getJSONArray("matches");
                            new ParseDataTask(arrayMatches).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });

        VolleyHelper.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);
    }

    private class ParseDataTask extends AsyncTask<Void,Void,ArrayList<Match>>{

        private final Gson gson = new Gson();
        private JSONArray array;

        public ParseDataTask(JSONArray array) {
            this.array = array;
        }

        @Override
        protected ArrayList<Match> doInBackground(Void... voids) {

            ArrayList<Match> list = new ArrayList<>();
            try {

                for (int i = 0; i < array.length(); i++) {
                    Log.d("MATCH i", array.get(i).toString());
                    Match match = (Match) gson.fromJson(array.get(i).toString(), Match.class);
                    list.add(match);
                }
                Log.d("MATCHES ON LIST", "N = " + list.size());
                Collections.reverse(list);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Match> matches) {
            super.onPostExecute(matches);
            matchesHistoryList.addAll(matches);
            recyclerAdapter.updateHistory(matches);
            scaleAdapter.notifyDataSetChanged();
            changeRefreshingValue(false);

        }


    }
}
