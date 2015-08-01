package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.ChampionStatsDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

public class ActivityChampionStatsDetails extends Activity {

    private ChampionStatsDto stats;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details);

        onRetrieveInstanceState(savedInstanceState);
        imageLoader = VolleyHelper.getInstance(this).getImageLoader();

        Log.d("ID CHAMP", Integer.toString(stats.getId()));

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
        imageView.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                        + ChampionsHandler.getServerVersion(this)
                        + "/img/champion/" + ChampionsHandler.getChampKey(stats.getId()) + ".png", imageLoader);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stats = (ChampionStatsDto) savedInstanceState.getSerializable("champData");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                stats = (ChampionStatsDto) intent.getSerializableExtra("champData");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
