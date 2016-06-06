package andiag.coru.es.welegends.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.persistance.DBSummoner;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySummoner extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = "ActivitySummoner";

    private String region;
    private DBSummoner db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);
        db = DBSummoner.getInstance(this);

        //Allow press Enter key to search
        startSummonerListener();

        //Region picker
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegions);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        region = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void startSummonerListener(){
        EditText editText = (EditText) findViewById(R.id.editTextSummoner);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    onClickFindSummoner(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void onClickFindSummoner(View v){
        String summonerName = ((EditText) findViewById(R.id.editTextSummoner)).getText().toString();
        if (Utils.isNetworkAvailable(this)) {
            summonerName = Utils.cleanString(summonerName);
            if ((!(summonerName.isEmpty())) && (!(summonerName.equals("")))) {
                Log.d(TAG,"Searching " + summonerName + " " + region);
                Summoner summoner = db.getSummonerByName(summonerName, region);
                if(summoner!=null && summoner.getSummonerLevel()==Utils.MAX_LEVEL){
                    Log.d(TAG, "Local Found: " + summoner.getId());
                    db.addSummoner(summoner);
                    //TODO load new screen
                }else {
                    Call<Summoner> call = RestClient.get(summonerName).getSummonerByName(region, summonerName);
                    call.enqueue(new Callback<Summoner>() {
                        @Override
                        public void onResponse(Call<Summoner> call, Response<Summoner> response) {
                            Summoner summoner = response.body();
                            summoner.setRegion(region);
                            db.addSummoner(summoner);
                            Log.d(TAG, "Searching: " + summoner.getName() + " --> Found: " + summoner.getId());
                        }
                        @Override
                        public void onFailure(Call<Summoner> call, Throwable t) {
                            Log.d(TAG, "Error loading Summoner Id");
                        }
                    });
                }

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.voidSummonerError),
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
        }
    }

}
