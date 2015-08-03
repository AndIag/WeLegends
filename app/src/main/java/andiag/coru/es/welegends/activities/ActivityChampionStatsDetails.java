package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import andiag.coru.es.welegends.DTOs.rankedStatsDTOs.ChampionStatsDto;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.StatsColor;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

public class ActivityChampionStatsDetails extends Activity {

    private Bundle stats;
        /*
        * champKey          String
        * name              String
        * victories         String
        * defeats           String
        * cs                String
        * gold              String
        * totalGames        float
        * kills             float
        * death             float
        * assist            float
        * penta             String
        * quadra            String
        * triple            String
        * double            String
        * image             int
        * */
    private ImageLoader imageLoader;
    private boolean isHeader;

    private void loadNotHeaderView() {
        setContentView(R.layout.activity_champion_details);

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
        TextView tname = (TextView) findViewById(R.id.textChamp);
        TextView tkills = (TextView) findViewById(R.id.textKills);
        TextView tdeath = (TextView) findViewById(R.id.textDeath);
        TextView tassist = (TextView) findViewById(R.id.textAssists);
        TextView tkda = (TextView) findViewById(R.id.textKDA);
        TextView tgold = (TextView) findViewById(R.id.textGold);
        TextView tcs = (TextView) findViewById(R.id.textCS);
        TextView tpercent = (TextView) findViewById(R.id.textPercent);
        TextView tKills = (TextView) findViewById(R.id.text1);
        TextView tDouble = (TextView) findViewById(R.id.text2);
        TextView tTriple = (TextView) findViewById(R.id.text3);
        TextView tQuadra = (TextView) findViewById(R.id.text4);
        TextView tPenta = (TextView) findViewById(R.id.text5);

        float totalGames, kills, death, assist;

        totalGames = stats.getFloat("totalGames");
        kills = stats.getFloat("kills");
        death = stats.getFloat("death");
        assist = stats.getFloat("assist");

        imageLoader = VolleyHelper.getInstance(this).getImageLoader();

        imageView.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                + ChampionsHandler.getServerVersion(this)
                + "/img/champion/" + stats.getString("champKey") + ".png", imageLoader);

        tname.setText(stats.getString("name"));

        tkills.setText(String.format("%.1f", kills / totalGames));
        tdeath.setText(String.format("%.1f", death / totalGames));
        tdeath.setTextColor(getResources().getColor(StatsColor.getColor(StatsColor.DEATHS, death / totalGames)));
        tassist.setText(String.format("%.1f", assist / totalGames));
        tkda.setText(String.format("%.2f", (kills + assist) / death));
        tkda.setTextColor(getResources().getColor(StatsColor.getColor(StatsColor.KDA, (kills + assist) / death)));
        tgold.setText(stats.getString("gold"));
        tcs.setText(stats.getString("cs"));
        tpercent.setText(String.format("%.1f", (Float.valueOf(stats.getString("victories"))/totalGames) * 100) + "%");
        tpercent.setTextColor(getResources().getColor(StatsColor.getColor(StatsColor.PERCENT, (Float.valueOf(stats.getString("victories")) / totalGames) * 100)));

        tKills.setText(String.format("%.0f", kills));
        tDouble.setText(stats.getString("double"));
        tTriple.setText(stats.getString("triple"));
        tQuadra.setText(stats.getString("quadra"));
        tPenta.setText(stats.getString("penta"));

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
        if(isHeader){
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
