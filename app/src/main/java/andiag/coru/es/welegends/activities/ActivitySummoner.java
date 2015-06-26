package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.dialogs.DialogAbout;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

public class ActivitySummoner extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private String region;

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

    public void onClickSummonerHistory(View v) {
        EditText editText = (EditText) findViewById(R.id.editTextSummoner);
        String summoner = editText.getText().toString();
        summoner = summoner.toLowerCase().replaceAll(" ", "").replace("\n", "").replace("\r", "");
        getSummonerId(summoner);

        //Intent i = new Intent(this, ActivityMain.class);
        //i.putExtra("SummonerName", summoner.toLowerCase().replaceAll(" ", "").replace("\n", "").replace("\r", ""));
        //i.putExtra("Region", region.toLowerCase());
        //startActivity(i);
    }

    private void getSummonerId(String summonerName){
        APIHandler handler = APIHandler.getInstance();

        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        String request = "https://" + region + handler.getServer() + region
                + handler.getSummonerByName() + summonerName + "?api_key=" + handler.getKey();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
