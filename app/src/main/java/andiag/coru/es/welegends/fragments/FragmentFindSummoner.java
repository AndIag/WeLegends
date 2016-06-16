package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.activities.ActivitySummonerData;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.persistence.Version;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFindSummoner extends Fragment implements AdapterView.OnItemSelectedListener {

    private final static String TAG = "FragmentFindSummoner";

    private ActivitySummoner parentActivity;
    private ActivitySummonerData notifiableActivity = null;
    private DBSummoner db;

    private String region;

    //region Callbacks
    private Callback<Summoner> callbackSearchSummoner = new Callback<Summoner>() {
        @Override
        public void onResponse(Call<Summoner> call, Response<Summoner> response) {
            Log.d(TAG, "onClickFindSummoner: Found-" + response.body().getId());
            Summoner summoner = response.body();
            summoner.setRegion(region);
            Log.d(TAG, "onClickFindSummoner: Updating database");
            db.addSummoner(summoner);
            parentActivity.throwNewActivity(summoner, false);
        }

        @Override
        public void onFailure(Call<Summoner> call, Throwable t) {
            Log.d(TAG, "onClickFindSummoner: Summoner not found");
            Toast.makeText(parentActivity.getApplicationContext(), getString(R.string.error404),
                    Toast.LENGTH_LONG).show();
        }
    };
    private Callback<Summoner> callbackUpdateSummoner = new Callback<Summoner>() {
        @Override
        public void onResponse(Call<Summoner> call, Response<Summoner> response) {
            Log.d(TAG, "onClickFindSummoner: Found-" + response.body().getId());
            Summoner newSummoner = response.body();
            newSummoner.setRegion(region);
            db.addSummoner(newSummoner);
            if (notifiableActivity == null) {
                Log.e(TAG, "callbackUpdateSummoner: Null noticeable activity. This should never happen");
                return;
            }
            notifiableActivity.notifySummonerDataChange(newSummoner);
        }

        @Override
        public void onFailure(Call<Summoner> call, Throwable t) {
            //This case may never happen
            Log.d(TAG, "onClickFindSummoner: Summoner not found");
        }
    };
    //endregion

    public FragmentFindSummoner() {
        // Required empty public constructor
    }

    public void setNotifiableActivity(ActivitySummonerData notifiableActivity) {
        this.notifiableActivity = notifiableActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parentActivity = (ActivitySummoner) context;
        this.db = ((ActivitySummoner) context).getDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_find_summoner, container, false);

        //Allow press Enter key to search
        startSummonerListener(fragmentView);

        //Show version
        TextView version = (TextView) fragmentView.findViewById(R.id.textVersion);
        version.setText("Version " + Version.getVersion(getActivity()));

        //Region picker
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.spinnerRegions);
        spinner.setOnItemSelectedListener(this);

        return fragmentView;
    }

    //region OnItemSelectedListener implementation
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        region = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    //endregion

    private void startSummonerListener(final View fragmentView) {
        EditText editText = (EditText) fragmentView.findViewById(R.id.editTextSummoner);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    findSummoner(fragmentView);
                    return true;
                }
                return false;
            }
        });
    }

    private void apiSearchSummonerId(String summonerName, String region, Callback<Summoner> callback) {
        Call<Summoner> call = RestClient.getWeLegendsData(summonerName).getSummonerByName(region, summonerName);
        call.enqueue(callback);
    }

    public void findSummoner(View fragmentView) {
        //Get and clean summoner
        String summonerName = ((EditText) fragmentView.findViewById(R.id.editTextSummoner)).getText().toString();
        summonerName = Utils.cleanString(summonerName);

        //TODO codify summoner name
        if (summonerName.isEmpty() || summonerName.equals("")) {
            Log.e(TAG, "onClickFindSummoner: Wrong search text");
            Toast.makeText(parentActivity.getApplicationContext(), getString(R.string.voidSummonerError),
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Check if network is available
        if (!Utils.isNetworkAvailable(parentActivity)) {
            Log.e(TAG, "onClickFindSummoner: Network unavailable");
            Toast.makeText(parentActivity.getApplicationContext(), getString(R.string.networkUnavailable),
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Try to find summoner in local database
        Log.d(TAG, "onClickFindSummoner: Searching " + summonerName + " " + region);
        final Summoner summoner = db.getSummonerByName(summonerName, region);

        if (summoner == null) { //We don't -> ask to the API
            apiSearchSummonerId(summonerName, region, callbackSearchSummoner);
            return;
        }

        //We do -> end.
        Log.d(TAG, "onClickFindSummoner: FoudInLocal-" + summoner.getId());
        Log.d(TAG, "onClickFindSummoner: Updating database");
        db.addSummoner(summoner);
        apiSearchSummonerId(summonerName, region, callbackUpdateSummoner);
        parentActivity.throwNewActivity(summoner, true);
    }

}
