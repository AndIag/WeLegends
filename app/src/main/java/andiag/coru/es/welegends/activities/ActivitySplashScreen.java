package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionListDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Andy on 26/06/2015.
 */
public class ActivitySplashScreen extends Activity {

    private String request;
    private ProgressBar progressBar;
    private TextView textView;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get actual time
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        textView = (TextView) this.findViewById(R.id.loadingText);

        APIHandler.getInstance(this);
        activity = this;

        getVersion();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void startActivity() {
        Intent mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivitySummoner.class);
        startActivity(mainIntent);
        finish();
    }

    private void getVersion() {
        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        request = handler.getServer() + handler.getVersions();

        progressBar.setVisibility(View.VISIBLE);
        textView.setText(getResources().getString(R.string.checkingVersion));

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> versions = gson.fromJson(response.toString()
                                , new TypeToken<ArrayList<String>>() {
                        }.getType());
                        if (versions != null && versions.get(0).equals(ChampionsHandler.getServerVersion())) {
                            try {
                                ChampionsHandler.setChampions(null, activity); //Initialize champions with our static data
                                startActivity();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, getResources().getString(R.string.error500)
                                        , Toast.LENGTH_LONG).show();
                                ActivitySplashScreen.this.finish();
                            }
                        } else {
                            //Get champions from server
                            getChampionsFromServer();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                ActivitySplashScreen.this.finish();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }

    private void getChampionsFromServer() {
        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        request = handler.getServer() + handler.getChampions();

        progressBar.setVisibility(View.VISIBLE);
        textView.setText(getResources().getString(R.string.loadNames));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ChampionsHandler.setChampions(gson.fromJson(response.toString(), ChampionListDto.class), activity);
                            startActivity();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, getResources().getString(R.string.error500)
                                    , Toast.LENGTH_LONG).show();
                            ActivitySplashScreen.this.finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                ActivitySplashScreen.this.finish();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }


}
