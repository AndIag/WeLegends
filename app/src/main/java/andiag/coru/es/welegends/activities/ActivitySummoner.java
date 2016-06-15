package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.fragments.FragmentFindSummoner;
import andiag.coru.es.welegends.fragments.FragmentSummonerHistoric;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.persistence.Version;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;
import andiag.coru.es.welegends.rest.entities.persist.Champion;
import andiag.coru.es.welegends.rest.utils.GenericStaticData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySummoner extends AppCompatActivity {

    private final static String TAG = "ActivitySummoner";
    private final static String FIND_SUMMONER_FRAGMENT = "findSummonerFragment";
    private final static String SUMMONER_HISTORIC_FRAGMENT = "summonerHistoricFragment";
    public static ActivitySummoner activity = null;

    private DBSummoner db;


    private Callback<List<String>> versionCallback = new Callback<List<String>>() {
        @Override
        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
            String newVersion = response.body().get(0);
            Log.d(TAG, "Found Version: " + newVersion);
            if (!newVersion.equals(Version.getVersion(activity))) {
                //Version.setVersion(newVersion, activity);
                //TODO load locales
            }
        }

        @Override
        public void onFailure(Call<List<String>> call, Throwable t) {
            Log.e(TAG, "VersionCallback ERROR");
        }
    };

    private Callback<GenericStaticData<String, Champion>> championsCallback = new Callback<GenericStaticData<String, Champion>>() {
        @Override
        public void onResponse(Call<GenericStaticData<String, Champion>> call, Response<GenericStaticData<String, Champion>> response) {
            //TODO save in database
            Log.d(TAG, String.valueOf(response.body().getData().keySet().size()));
        }

        @Override
        public void onFailure(Call<GenericStaticData<String, Champion>> call, Throwable t) {

        }
    };

    public DBSummoner getDb() {
        return db;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivitySummoner.activity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_fragments);
        db = DBSummoner.getInstance(this);

        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new FragmentFindSummoner(), FIND_SUMMONER_FRAGMENT).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentFindSummoner, new FragmentFindSummoner(), FIND_SUMMONER_FRAGMENT)
                    .add(R.id.fragmentSummonerHistoric, new FragmentSummonerHistoric(), SUMMONER_HISTORIC_FRAGMENT)
                    .commit();
        }

        checkServerVersion();

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
        Call<List<String>> call = RestClient.get().getServerVersion();
        call.enqueue(versionCallback);
    }

    private void loadServerChampions(String version, String locale) {
        Call<GenericStaticData<String, Champion>> call = RestClient.getStatic(version, locale).getChampions();
        call.enqueue(championsCallback);
    }
    //endregion

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
}
