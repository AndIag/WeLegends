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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.dialogs.DialogAbout;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

public class ActivitySummoner extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private String region;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_summoner);

        getWindow().setBackgroundDrawable(null);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegions);
        spinner.setOnItemSelectedListener(this);

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
            getSummonerId(summoner);
        } else {
            Toast.makeText(this, "Network is unavailable!", Toast.LENGTH_LONG).show();
        }
    }

    private void startMainActivity(Summoner summoner) {
        Intent i = new Intent(this, ActivityMain.class);
        i.putExtra("summoner", summoner);
        i.putExtra("region", region.toLowerCase());
        startActivity(i);
    }

    private void getSummonerId(final String summonerName){
        if (isLoading) return;

        isLoading = true;

        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        String url = "https://" + region + handler.getServer() + region
                + handler.getSummonerByName() + summonerName + "?api_key=" + handler.getKey();


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

}
