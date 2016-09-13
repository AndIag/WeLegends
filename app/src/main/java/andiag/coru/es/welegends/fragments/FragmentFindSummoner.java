package andiag.coru.es.welegends.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.activities.ActivityNotifiable;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.persistence.Version;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import andiag.coru.es.welegends.views.FontTextView;
import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentFindSummoner extends FragmentBase implements AdapterView.OnItemSelectedListener, FragmentNotifiable<Summoner> {

    private final static String TAG = "FragmentFindSummoner";
    @BindView(R.id.imageBackground)
    ImageView background;
    @BindView(R.id.editTextSummoner)
    EditText summonerEditText;
    @BindView(R.id.spinnerRegions)
    Spinner spinnerRegions;
    @BindView(R.id.textVersion)
    FontTextView textVersion;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.buttonHistoric)
    ImageButton buttonHistoric;

    private ActivityNotifiable<Summoner> activityNotifiable = null;
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
            ((ActivitySummoner)parentActivity).launchNewActivity(summoner, null);
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
            if (activityNotifiable == null) {
                Log.e(TAG, "callbackUpdateSummoner: Null noticeable activity. This should never happen");
                return;
            }
            activityNotifiable.notifyDataChange(newSummoner);
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

    @Override
    public void setActivityNotifiable(ActivityNotifiable<Summoner> activityNotifiable) {
        this.activityNotifiable = activityNotifiable;
    }

    @Override
    public void setProgressBarState(int viewState) {
        if (progressBar != null) {
            progressBar.setVisibility(viewState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.db = ((ActivitySummoner) context).getDb();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_find_summoner;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.GONE);
        setBackground("Tristana_5.jpg");

        startSummonerListener();

        textVersion.setText("Version " + Version.getVersion(parentActivity));

        spinnerRegions.setOnItemSelectedListener(this);

        //If we are showing both fragments hide history button
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            buttonHistoric.setVisibility(View.GONE);
        }
    }

    private void setBackground(String image){
        String endpoint;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            endpoint = RestClient.getSplashImgEndpoint() + image;
        } else endpoint = RestClient.getLoadingImgEndpoint() + image;
        Glide.with(parentActivity).load(endpoint).dontAnimate()
                .placeholder(R.drawable.background_default1).error(R.drawable.background_default1)
                .into(background);
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

    private void startSummonerListener() {
        summonerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    findSummoner();
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

    public void findSummoner() {
        //Get and clean summoner
        String summonerName = summonerEditText.getText().toString();
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
        Log.d(TAG, "onClickFindSummoner: FoundInLocal-" + summoner.getId());
        Log.d(TAG, "onClickFindSummoner: Updating database");
        db.addSummoner(summoner);
        apiSearchSummonerId(summonerName, region, callbackUpdateSummoner);
        ((ActivitySummoner)parentActivity).launchNewActivity(summoner, ActivitySummoner.FIND_SUMMONER_FRAGMENT);
    }

}
