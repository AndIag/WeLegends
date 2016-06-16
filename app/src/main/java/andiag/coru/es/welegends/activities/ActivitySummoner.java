package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.Utils;
import andiag.coru.es.welegends.fragments.FragmentFindSummoner;
import andiag.coru.es.welegends.fragments.FragmentSummonerHistoric;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.persistence.Version;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import andiag.coru.es.welegends.rest.entities.persist.Champion;
import andiag.coru.es.welegends.rest.utils.GenericStaticData;
import bolts.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySummoner extends AppCompatActivity {

    private final static String TAG = "ActivitySummoner";
    private final static String FIND_SUMMONER_FRAGMENT = "findSummonerFragment";
    private final static String SUMMONER_HISTORIC_FRAGMENT = "summonerHistoricFragment";
    public static ActivitySummoner activity = null;

    private DBSummoner db;

    public DBSummoner getDb() {
        if (db == null) {
            db = DBSummoner.getInstance(this);
        }
        return db;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivitySummoner.activity = this;

        //TODO make this exec just one time per run
        checkServerVersion();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);
        db = DBSummoner.getInstance(this);

        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new FragmentFindSummoner(), FIND_SUMMONER_FRAGMENT).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentFindSummoner, new FragmentFindSummoner(), FIND_SUMMONER_FRAGMENT)
                    .add(R.id.fragmentSummonerHistoric, new FragmentSummonerHistoric(), SUMMONER_HISTORIC_FRAGMENT)
                    .commit();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivitySummoner.activity = null;
        this.db = null;
    }

    public void throwNewActivity(Summoner summoner, boolean updateRequired) {
        Intent i = new Intent(this, ActivitySummonerData.class);
        if (updateRequired) {
            i.putExtra(ActivitySummonerData.ARG_FRAGMENT, FIND_SUMMONER_FRAGMENT);
        }
        i.putExtra(ActivitySummonerData.ARG_SUMMONER, summoner);
        startActivity(i);
    }

    //region Server Static Data
    private void checkServerVersion() {
        Call<List<String>> call = RestClient.getWeLegendsData().getServerVersion();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, final Response<List<String>> response) {
                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        String newVersion = response.body().get(0);
                        Log.d(TAG, "checkServerVersion -> FOUND: " + newVersion);
                        if (!newVersion.equals(Version.getVersion(activity))) {
                            Version.setVersion(newVersion, activity);
                            Log.d(TAG, "checkServerVersion -> NEW VERSION -> LOAD DATA");
                            //Version.setVersion(newVersion, activity);
                            String locale = getResources().getConfiguration().locale.getLanguage() + "_" + getResources().getConfiguration().locale.getCountry();

                            loadServerChampions(newVersion, locale);
                            //TODO load other data
                            return null;
                        }
                        stopProgressBar();
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e(TAG, "checkServerVersion -> FAIL");
            }
        });
    }

    private void loadServerChampions(final String version, final String locale) {
        Call<GenericStaticData<String, Champion>> call = RestClient.getDdragonStaticData(version, locale).getChampions();
        call.enqueue(new Callback<GenericStaticData<String, Champion>>() {
            @Override
            public void onResponse(Call<GenericStaticData<String, Champion>> call, final Response<GenericStaticData<String, Champion>> response) {
                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if (response.body() == null) {
                            Log.d(TAG, "loadServerChampions -> FAIL");
                            if (!locale.equals(Utils.DEFAULT_LOCALE)) {
                                Log.d(TAG, "loadServerChampions -> RELOAD WITH: " + Utils.DEFAULT_LOCALE);
                                loadServerChampions(version, Utils.DEFAULT_LOCALE);
                            }
                        }
                        Log.d(TAG, "loadServerChampions -> LOADED: " + String.valueOf(response.body().getData().size()));
                        //TODO save in database
                        return null;
                    }
                });
            }

            @Override
            public void onFailure(Call<GenericStaticData<String, Champion>> call, Throwable t) {
                Log.d(TAG, "loadServerChampions -> FAIL");
                if (!locale.equals(Utils.DEFAULT_LOCALE)) {
                    Log.d(TAG, "loadServerChampions -> RELOAD WITH: " + Utils.DEFAULT_LOCALE);
                    loadServerChampions(version, Utils.DEFAULT_LOCALE);
                }
            }
        });
    }
    //endregion

    //region View Control

    public void stopProgressBar() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);
    }

    public void startProgressBar() {
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
    }

    //Redirect View action to fragment
    public void onClickFindSummoner(View v){
        FragmentFindSummoner fragmentFindSummoner = (FragmentFindSummoner) getSupportFragmentManager()
                .findFragmentByTag(FIND_SUMMONER_FRAGMENT);

        fragmentFindSummoner.findSummoner(fragmentFindSummoner.getView());
    }

    public void onClickSwapFragment(View view) {
        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentSummonerHistoric())
                    .addToBackStack(null)
                    .commit();
        }
    }
    //endregion
}
