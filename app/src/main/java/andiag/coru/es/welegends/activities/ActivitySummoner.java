package andiag.coru.es.welegends.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import andiag.coru.es.welegends.DTOs.SummonerHistory;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.adapters.AdapterSummoner;
import andiag.coru.es.welegends.dialogs.DialogAbout;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.utils.history.HistoryHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

public class ActivitySummoner extends ActionBarActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static ActivityMain activityMain;
    private String region;
    private boolean isLoading = false;
    private ArrayList<SummonerHistory> history;
    private ListView listSummoners;
    private AdapterSummoner adapter;

    public static void setActivityMain(ActivityMain a) {
        activityMain = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_summoner);

        getWindow().setBackgroundDrawable(null);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegions);
        spinner.setOnItemSelectedListener(this);

        listSummoners = (ListView) findViewById(R.id.listViewSummHistory);
        adapter = new AdapterSummoner(this);
        listSummoners.setAdapter(adapter);

        listSummoners.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //Cargar el valor del historial de summoners
        try {
            history = HistoryHandler.getHistory(this);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.summonerHistoryError),
                    Toast.LENGTH_LONG).show();
            history = new ArrayList<>();
        }

        adapter.updateSummoners(history);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_summoner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //About Dialog
        if (id == R.id.action_about) {
            DialogAbout dialogAbout = DialogAbout.newInstance();
            //dialogAbout.show(this.getFragmentManager(), "DialogAbout");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public void onClickSummonerHistory(View v) {

        if (isNetworkAvailable()) {
            EditText editText = (EditText) findViewById(R.id.editTextSummoner);
            String summoner = editText.getText().toString();
            summoner = summoner.toLowerCase().replaceAll(" ", "").replace("\n", "").replace("\r", "");
            if ((!(summoner == "")) && (!(summoner == null))) {
                getSummonerId(summoner);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.voidSummonerError),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private void startMainActivity(Summoner summoner) {
        SummonerHistory summonerHistory = new SummonerHistory();
        summonerHistory.setSummoner(summoner);
        summonerHistory.setTimestamp(Calendar.getInstance().getTimeInMillis());
        summonerHistory.setRegion(region.toLowerCase());
        history.add(summonerHistory);

        activityMain.setSummoner(summoner);

        //Guardar el valor del historial de summoners
        try {
            HistoryHandler.setHistory(this, history);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isLoading = false;
    }

    private void getSummonerId(final String summonerName){
        if (isLoading) return;

        isLoading = true;

        //Iniciamos la ativity
        Intent i = new Intent(this, ActivityMain.class);
        i.putExtra("region", region.toLowerCase());
        i.putExtra("summonerName", summonerName);
        startActivity(i);

        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        String url = handler.getServer() + region.toLowerCase() + handler.getSummoner() + summonerName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject summonerJSON = response.getJSONObject(summonerName);
                            Summoner summoner = (Summoner) gson.fromJson(summonerJSON.toString(), Summoner.class);
                            Log.d("RESPUESTA", summoner.getId() + "---" + summoner.getName());
                            startMainActivity(summoner);
                            isLoading = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.loadingSummonerError),
                        Toast.LENGTH_LONG).show();
                if (activityMain != null) {
                    activityMain.finish();
                }
                isLoading = false;
            }
        });

        VolleyHelper.getInstance(this).getRequestQueue().add(jsonObjectRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        region = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Dialog About Methods
    public void onTwitterIagoClick(View view) {
        DialogAbout.onTwitterIagoClick(this);
    }

    public void onTwitterAndyClick(View view) {
        DialogAbout.onTwitterAndyClick(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SummonerHistory summonerHistory = history.get(i);

        Intent in = new Intent(this, ActivityMain.class);
        in.putExtra("region", summonerHistory.getRegion().toLowerCase());
        in.putExtra("summoner", summonerHistory.getSummoner());
        startActivity(in);

    }
}
