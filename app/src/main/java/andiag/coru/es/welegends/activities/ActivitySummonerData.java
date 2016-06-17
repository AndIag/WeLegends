package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.fragments.NotifiableFragment;
import andiag.coru.es.welegends.rest.entities.Summoner;

public class ActivitySummonerData extends AppCompatActivity implements NotifiableActivity<Summoner> {

    private final static String TAG = "ActivitySummonerData";
    public final static String ARG_SUMMONER = "SUMMONER";
    public final static String ARG_FRAGMENT = "FRAGMENT";

    private Summoner summoner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_data);

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(ARG_FRAGMENT)) {
                ((NotifiableFragment) ActivitySummoner.activity.getSupportFragmentManager()
                        .findFragmentByTag(getIntent().getStringExtra(ARG_FRAGMENT))).setNotifiableActivity(this);
            }
            this.summoner = (Summoner) getIntent().getSerializableExtra(ARG_SUMMONER);
            Log.d(TAG, "received: " + summoner.getId());
        }

    }

    public void notifyDataChange(Summoner newSummoner) {
        Log.d(TAG, "notifySummonerDataChange: " + summoner.getId());
        this.summoner = newSummoner;
        //TODO notify view
    }

}
