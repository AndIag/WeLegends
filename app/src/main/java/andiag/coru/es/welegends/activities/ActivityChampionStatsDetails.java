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

    private Bundle stats;
    private ImageLoader imageLoader;
    private boolean isHeader;

    private void loadNotHeaderView() {
        setContentView(R.layout.activity_champion_details);

        imageLoader = VolleyHelper.getInstance(this).getImageLoader();

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
        imageView.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                + ChampionsHandler.getServerVersion(this)
                + "/img/champion/" + stats.getString("champKey") + ".png", imageLoader);

    }

    private void loadHeaderView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);


        if(!isHeader){
            loadNotHeaderView();
        }
        if(!isHeader){
            loadHeaderView();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putBundle("champData", stats);
    }

    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stats = savedInstanceState.getBundle("champData");
        }else{
            Bundle extras = getIntent().getExtras();

            isHeader = extras.getBoolean("isHeader");
            if(!isHeader) {
                stats = extras.getBundle("champData");
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
