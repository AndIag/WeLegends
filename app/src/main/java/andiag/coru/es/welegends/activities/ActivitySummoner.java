package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySummoner extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = "ActivitySummoner";

    private String region;
    private DBSummoner db;

    //region Callbacks

    private Callback<Summoner> callbackSearchSummoner = new Callback<Summoner>() {
        @Override
        public void onResponse(Call<Summoner> call, Response<Summoner> response) {
            Log.d(TAG, "onClickFindSummoner: Found-" + response.body().getId());
            Summoner summoner = response.body();
            summoner.setRegion(region);
            Log.d(TAG, "onClickFindSummoner: Updating database");
            db.addSummoner(summoner);
            //TODO load new screen #1
        }
        @Override
        public void onFailure(Call<Summoner> call, Throwable t) {
            Log.d(TAG, "onClickFindSummoner: Summoner not found");
            Toast.makeText(getApplicationContext(), getString(R.string.error404),
                    Toast.LENGTH_LONG).show();
        }
    };
    private Callback<Summoner> callbackUpdateSummoner = new Callback<Summoner>() {
        @Override
        public void onResponse(Call<Summoner> call, Response<Summoner> response) {
            //TODO move this to background processing
            Log.d(TAG, "onClickFindSummoner: Found-" + response.body().getId());
            Summoner newSummoner = response.body();
            newSummoner.setRegion(region);
            //TODO notify fragment
        }
        @Override
        public void onFailure(Call<Summoner> call, Throwable t) {
            //This case may never happen
            Log.d(TAG, "onClickFindSummoner: Summoner not found");
        }
    };

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_new);
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

    private void apiSearchSummonerId(String summonerName, String region, Callback<Summoner> callback){
        Call<Summoner> call = RestClient.get(summonerName).getSummonerByName(region, summonerName);
        call.enqueue(callback);
    }

    public void onClickFindSummoner(View v){
        //Get and clean summoner
        String summonerName = ((EditText) findViewById(R.id.editTextSummoner)).getText().toString();
        summonerName = Utils.cleanString(summonerName);

        if(summonerName.isEmpty() || summonerName.equals("")){
            Log.e(TAG, "onClickFindSummoner: Wrong search text");
            Toast.makeText(getApplicationContext(), getString(R.string.voidSummonerError),
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Check if network is available
        if (!Utils.isNetworkAvailable(this)){
            Log.e(TAG, "onClickFindSummoner: Network unavailable");
            Toast.makeText(this, getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
            return;
        }

        //Try to find summoner in local database
        Log.d(TAG,"onClickFindSummoner: Searching " + summonerName + " " + region);
        final Summoner summoner = db.getSummonerByName(summonerName, region);

        if(summoner==null){ //We don't -> ask to the API
            apiSearchSummonerId(summonerName, region, callbackSearchSummoner);
            return;
        }

        //We do -> end.
        Log.d(TAG, "onClickFindSummoner: FoudInLocal-" + summoner.getId());
        Log.d(TAG, "onClickFindSummoner: Updating database");
        db.addSummoner(summoner);
        //TODO load new screen #1
        apiSearchSummonerId(summonerName, region, callbackUpdateSummoner);
    }

}
