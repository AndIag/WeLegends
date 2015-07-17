package andiag.coru.es.welegends.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.RankedStatsDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.entities.League;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentPlayerStats extends SwipeRefreshLayoutFragment {

    private static FragmentPlayerStats fragmentPlayerStats;
    private static ActivityMain activityMain;

    private View rootView;

    private ImageLoader imageLoader;
    private APIHandler apiHandler;
    private Summoner summoner;
    private boolean isLoading;
    private ArrayList<League> leagues = new ArrayList<>();

    public FragmentPlayerStats() {
        imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
        apiHandler = APIHandler.getInstance();
    }

    public static void deleteFragment() {
        fragmentPlayerStats = null;
    }

    public static FragmentPlayerStats getInstance(ActivityMain aM) {
        activityMain = aM;
        if (fragmentPlayerStats != null) {
            return fragmentPlayerStats;
        }
        fragmentPlayerStats = new FragmentPlayerStats();
        return fragmentPlayerStats;
    }

    public void setSummoner(Summoner summoner) {
        this.summoner = summoner;
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors

        changeRefreshingValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_player_stats, container, false);

        initializeRefresh(rootView);

        CircledNetworkImageView networkImg = (CircledNetworkImageView) rootView.findViewById(R.id.imageSummoner);
        networkImg.setErrorImageResId(R.drawable.item_default);
        networkImg.setDefaultImageResId(R.drawable.item_default);

        TextView txtName = (TextView) rootView.findViewById(R.id.textSummonerName);
        TextView txtLevel = (TextView) rootView.findViewById(R.id.textLevel);

        if (summoner != null) {
            networkImg.setImageUrl(apiHandler.getServer() + apiHandler.getIcon() + summoner.getProfileIconId(), imageLoader);
            txtName.setText(summoner.getName());
            txtLevel.setText(getString(R.string.level)+" "+summoner.getSummonerLevel());

            getLeagues();
        }

        return rootView;
    }

    private void setInfoInView(){
        TextView txtSolo = (TextView) rootView.findViewById(R.id.textSolo);
        //TextView txt5vs5 = (TextView) rootView.findViewById(R.id.text5vs5);
        //TextView txt3vs3 = (TextView) rootView.findViewById(R.id.text3vs3);

        for(League l : leagues){
            switch (l.getQueue()){
                case "RANKED_SOLO_5x5":
                    setLeagueSolo(l);
                    txtSolo.setText("RANKED SOLO");
                    break;
                /*case "RANKED_TEAM_3x3" : txt3vs3.setText("3vs3");
                    break;
                case "RANKED_TEAM_5x5" : txt5vs5.setText("5vs5");
                    break;*/
            }
        }

    }

    private void setLeagueSolo(League l){
        ImageView image = (ImageView) rootView.findViewById(R.id.imageSolo);
        String imres = l.getTier() + l.getEntries().get(0).getDivision();
        int id = activityMain.getResources().getIdentifier(imres.toLowerCase(),"drawable",activityMain.getPackageName());
        image.setImageResource(id);
    }

    private void getStats() {
        if (isLoading) return;

        isLoading = true;

        final Gson gson = new Gson();

        changeRefreshingValue(true);
        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        String request = handler.getServer() + handler.getStats() + summoner.getId() + handler.getStats_ranked();
        //String request = handler.getServer() + handler.getStats() + summoner.getId() + handler.getStats_global();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        RankedStatsDto rankedStatsDto = gson.fromJson(String.valueOf(response), RankedStatsDto.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                isLoading = false;
                changeRefreshingValue(false);
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


    //NEEDED METHODS
    private void getLeagues() {
        if (isLoading) return;

        isLoading = true;

        final Gson gson = new Gson();

        changeRefreshingValue(true);
        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(activityMain);
        }

        String request = handler.getServer() + activityMain.getRegion().toLowerCase() + handler.getLeagues() + summoner.getId();

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
                            isLoading = false;
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

}
