package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.adapters.AdapterListHeader;
import andiag.coru.es.welegends.entities.ItemLeague;
import andiag.coru.es.welegends.entities.ItemSection;
import andiag.coru.es.welegends.entities.League;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.entities.utils.Item;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentPlayerStats extends SwipeRefreshLayoutFragment {

    private static ActivityMain activityMain;
    //NEEDED METHODS
    private final Gson gson = new Gson();
    private View rootView;
    private ImageLoader imageLoader;
    private APIHandler apiHandler;
    private AdapterListHeader adapter;
    private ListView listView;
    //ARGUMENTS
    private Summoner summoner;
    private String region;

    private ArrayList<League> leagues = new ArrayList<>();
    private String request;

    public FragmentPlayerStats() {
        imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
        apiHandler = APIHandler.getInstance();
    }

    public static FragmentPlayerStats newInstance(String region, Summoner summoner) {
        FragmentPlayerStats fragmentPlayerStats = new FragmentPlayerStats();
        Bundle args = new Bundle();
        args.putSerializable("summoner", summoner);
        args.putString("region", region);
        fragmentPlayerStats.setArguments(args);
        return fragmentPlayerStats;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leagues.clear();
                getLeagues();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("region", region);
        outState.putSerializable("summoner", summoner);
        outState.putSerializable("leagues", leagues);
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
        adapter = new AdapterListHeader(activityMain);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (apiHandler == null) {
            apiHandler = APIHandler.getInstance();
            if (apiHandler == null) {
                apiHandler = APIHandler.getInstance(activityMain);
            }
        }
        if (imageLoader == null) {
            imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
        }
        if (leagues.isEmpty()){
            getLeagues();
        } else setInfoInView();
    }

    @Override //Si se ejecuta al cambiar 2 fragments para el lado
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_stats, container, false);

        initializeRefresh(rootView);

        CircledNetworkImageView networkImg = (CircledNetworkImageView) rootView.findViewById(R.id.imageSummoner);
        networkImg.setErrorImageResId(R.drawable.item_default);
        networkImg.setDefaultImageResId(R.drawable.item_default);

        TextView txtName = (TextView) rootView.findViewById(R.id.textSummonerName);
        TextView txtLevel = (TextView) rootView.findViewById(R.id.textLevel);

        listView = (ListView) rootView.findViewById(R.id.listViewLeagues);
        listView.setAdapter(adapter);

        if (summoner != null) {
            networkImg.setImageUrl(apiHandler.getServer() + apiHandler.getIcon() + summoner.getProfileIconId(), imageLoader);
            txtName.setText(summoner.getName());
            txtLevel.setText(getString(R.string.level)+" "+summoner.getSummonerLevel());

            if (leagues != null && leagues.size() == 0) {
                //Tenemos que cargar las ligas
                getLeagues();
            } else {
                //Ya las teniamos cargadas
                if (leagues == null) {
                    leagues = new ArrayList<>();
                }
                setInfoInView();
            }
        }

        return rootView;
    }

    private void setInfoInView(){

        int pos5 = -1, pos3 = -1;

        ArrayList<Item> groups = new ArrayList<>();

        Item itemSection;
        for(League l : leagues){
            switch (l.getQueue()){
                case "RANKED_SOLO_5x5":
                    groups.add(new ItemSection("Solo"));
                    groups.add(new ItemLeague(l));
                    break;
                case "RANKED_TEAM_3x3":
                    if (pos3 < 0) {
                        itemSection = new ItemSection("Team 3vs3");
                        groups.add(itemSection);
                        pos3 = groups.indexOf(itemSection);
                    }
                    groups.add(pos3 + 1, new ItemLeague(l));
                    break;
                case "RANKED_TEAM_5x5":
                    if (pos5 < 0) {
                        itemSection = new ItemSection("Team 5vs5");
                        groups.add(itemSection);
                        pos5 = groups.indexOf(itemSection);
                    }
                    groups.add(pos5 + 1, new ItemLeague(l));
                    break;
            }
        }

        adapter.updateItems(groups);
        setListViewHeightBasedOnItems(listView);

    }

    private void getLeagues() {
        if (isLoading()) return;

        changeRefreshingValue(true);

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        request = handler.getServer() + activityMain.getRegion().toLowerCase() + handler.getLeagues() + summoner.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
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

}
