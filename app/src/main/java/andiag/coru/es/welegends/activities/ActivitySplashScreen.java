package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import andiag.coru.es.welegends.DTOs.championsDTOs.ChampionListDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

/**
 * Created by Andy on 26/06/2015.
 */
public class ActivitySplashScreen extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 2000;
    String request;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get actual time
        long t = Calendar.getInstance().getTimeInMillis();

        super.onCreate(savedInstanceState);

        APIHandler.getInstance(this);
        getVersion();

        //Poner pantalla vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Iniciar actividad principal y finalizar splash
                Intent mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivitySummoner.class);
                startActivity(mainIntent);
                finish();
            }
        };

        //Set the new delay time
        long delay = SPLASH_SCREEN_DELAY - (Calendar.getInstance().getTimeInMillis() - t);
        if (delay > 0) {
            Timer timer = new Timer();
            timer.schedule(task, delay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getVersion() {
        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        activity = this;

        request = handler.getServer() + handler.getVersions();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<String> versions = gson.fromJson(response.toString()
                                , new TypeToken<ArrayList<String>>() {
                        }.getType());
                        if (versions != null && versions.get(0).equals(ChampionsHandler.getServerVersion(activity))) {
                            try {
                                ChampionsHandler.setChampions(null, activity); //Initialize champions with our static data
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Get champions from server
                            getChampionsFromServer();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String message = activity.getString(R.string.errorDefault);
                    switch (networkResponse.statusCode) {
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                            message = activity.getString(R.string.error500);
                            break;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE:
                            message = activity.getString(R.string.error503);
                            break;
                    }
                    Toast.makeText(activity, message
                            , Toast.LENGTH_LONG).show();
                }
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ChampionsHandler.setChampions(gson.fromJson(response.toString(), ChampionListDto.class), activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    String message = activity.getString(R.string.errorDefault);
                    switch (networkResponse.statusCode) {
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                            message = activity.getString(R.string.error500);
                            break;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE:
                            message = activity.getString(R.string.error503);
                            break;
                    }
                    Toast.makeText(activity, message
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(this).getRequestQueue().add(jsonObjectRequest);
    }


}
