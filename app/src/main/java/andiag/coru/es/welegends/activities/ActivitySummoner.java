package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.fragments.FragmentFindSummoner;
import andiag.coru.es.welegends.persistence.DBSummoner;
import andiag.coru.es.welegends.rest.entities.Summoner;

public class ActivitySummoner extends AppCompatActivity {

    private final static String TAG = "ActivitySummoner";
    private final static String FIND_SUMMONER_FRAGMENT = "findSummonerFragment";
    private final static String SUMMONER_HISTORIC_FRAGMENT = "summonerHistoricFragment";

    private DBSummoner db;

    public DBSummoner getDb() {
        return db;
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
                    .add(R.id.fragmentSummonerHistoric, new Fragment(), SUMMONER_HISTORIC_FRAGMENT)
                    .commit();
        }

    }

    public void throwNewActivity(Summoner summoner) {
        //TODO load new screen #1
    }

    //Redirect View action to fragment
    public void onClickFindSummoner(View v){
        FragmentFindSummoner fragmentFindSummoner = (FragmentFindSummoner) getSupportFragmentManager()
                .findFragmentByTag(FIND_SUMMONER_FRAGMENT);

        fragmentFindSummoner.findSummoner(fragmentFindSummoner.getView());
    }

    public void onClickSwapFragment(View view) {
        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new Fragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
