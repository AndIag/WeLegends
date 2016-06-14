package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.fragments.FragmentFindSummoner;
import andiag.coru.es.welegends.rest.entities.Summoner;

public class ActivitySummonerData extends AppCompatActivity {

    private final static String TAG = "ActivitySummonerData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_data);

        if (savedInstanceState == null) {
            ((FragmentFindSummoner) ActivitySummoner.activity.getSupportFragmentManager()
                    .findFragmentByTag("findSummonerFragment")).setNotificableActivity(this);
        }

    }

    public void notifySummonerDataChange(Summoner summoner) {
        Log.d(TAG, "notifySummonerDataChange: " + summoner.getId());
    }

}
