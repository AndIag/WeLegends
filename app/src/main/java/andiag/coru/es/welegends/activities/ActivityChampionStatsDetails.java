package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.handlers.API;
import andiag.coru.es.welegends.utils.handlers.StatsColor;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

public class ActivityChampionStatsDetails extends Activity {

    private Bundle stats;
    /*      BUNDLE DATA
    * summonerName      Summoner *solo en la header
    * summonerProfileId int *solo en la header
    * champId           int
    * name              String
    * key               String
    * victories         float
    * defeats           float
    * totalGames        float
    * kills             float
    * death             float
    * assist            float
    * cs                float
    * gold              float
    * penta             int
    * quadra            int
    * triple            int
    * double            int
    * turrets           int
    * dealt             int
    * taken             int
    * */
    private ImageLoader imageLoader;
    private boolean isHeader = false;

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
        TextView tTurrets = (TextView) findViewById(R.id.textTurrets);

        TextView tDealt = (TextView) findViewById(R.id.textDamageDealt);
        TextView tTaken = (TextView) findViewById(R.id.textDamageTaken);

        float totalGames, kills, death, assist;

        totalGames = stats.getFloat("totalGames");
        kills = stats.getFloat("kills");
        death = stats.getFloat("death");
        assist = stats.getFloat("assist");

        imageView.setImageUrl(API.getChampionIcon(stats.getString("key")), imageLoader);

        tname.setText(stats.getString("name"));

        tkills.setText(String.format("%.1f", kills / totalGames));
        tkills.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));
        tdeath.setText(String.format("%.1f", death / totalGames));
        tdeath.setTextColor(getResources().getColor(StatsColor.getDeathColor(death / totalGames, 1800)));
        tassist.setText(String.format("%.1f", assist / totalGames));
        tassist.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));
        if (death == 0) death = 1;
        tkda.setText(String.format("%.2f", (kills + assist) / death));
        tkda.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));

        tgold.setText(String.format("%.1f", stats.getFloat("gold") / (totalGames * 1000)) + "k");
        tgold.setTextColor(getResources().getColor(StatsColor.getGoldColor(stats.getFloat("gold") / totalGames, 1800, -1)));
        tcs.setText(String.format("%.1f", stats.getFloat("cs") / totalGames));
        tcs.setTextColor(getResources().getColor(StatsColor.getCSColor(stats.getFloat("cs") / totalGames, 1800, -1)));

        tpercent.setText(String.format("%.1f", (stats.getFloat("victories") / totalGames) * 100) + "%");
        tpercent.setTextColor(getResources().getColor(StatsColor.getPercentColor((stats.getFloat("victories") / totalGames) * 100)));

        tKills.setText(String.format("%.0f", kills));
        tDouble.setText(String.valueOf(stats.getInt("double")));
        tTriple.setText(String.valueOf(stats.getInt("triple")));
        tQuadra.setText(String.valueOf(stats.getInt("quadra")));
        tPenta.setText(String.valueOf(stats.getInt("penta")));
        tTurrets.setText(String.valueOf(stats.getInt("turrets")));

        tDealt.setText(String.format("%.0f", stats.getInt("dealt") / totalGames));
        tTaken.setText(String.format("%.0f", stats.getInt("taken") / totalGames));

    }

    private void loadHeaderView() {
        setContentView(R.layout.activity_champion_details_header);

        isHeader = true;

        CircledNetworkImageView imageView = (CircledNetworkImageView) findViewById(R.id.imageView);
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
        TextView tTurrets = (TextView) findViewById(R.id.textTurrets);

        TextView tDealt = (TextView) findViewById(R.id.textDamageDealt);
        TextView tTaken = (TextView) findViewById(R.id.textDamageTaken);

        float totalGames, kills, death, assist;

        totalGames = stats.getFloat("totalGames");
        kills = stats.getFloat("kills");
        death = stats.getFloat("death");
        assist = stats.getFloat("assist");

        imageView.setErrorImageResId(R.drawable.default_champion_error);
        imageView.setDefaultImageResId(R.drawable.default_champion);
        imageView.setImageUrl(API.getProfileIcon(stats.getLong("summonerProfileId")), imageLoader);

        tname.setText(stats.getString("summonerName"));

        tkills.setText(String.format("%.1f", kills / totalGames));
        tkills.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));
        tdeath.setText(String.format("%.1f", death / totalGames));
        tdeath.setTextColor(getResources().getColor(StatsColor.getDeathColor(death / totalGames, 1800)));
        tassist.setText(String.format("%.1f", assist / totalGames));
        tassist.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));
        if (death == 0) death = 1;
        tkda.setText(String.format("%.2f", (kills + assist) / death));
        tkda.setTextColor(getResources().getColor(StatsColor.getKDAColor((kills + assist) / death)));

        tgold.setText(String.format("%.1f", stats.getFloat("gold") / (totalGames * 1000)) + "k");
        tgold.setTextColor(getResources().getColor(StatsColor.getGoldColor(stats.getFloat("gold") / totalGames, 1800, -1)));
        tcs.setText(String.format("%.1f", stats.getFloat("cs") / totalGames));
        tcs.setTextColor(getResources().getColor(StatsColor.getCSColor(stats.getFloat("cs") / totalGames, 1800, -1)));

        tpercent.setText(String.format("%.1f", (stats.getFloat("victories") / totalGames) * 100) + "%");
        tpercent.setTextColor(getResources().getColor(StatsColor.getPercentColor((stats.getFloat("victories") / totalGames) * 100)));

        tKills.setText(String.format("%.0f", kills));
        tDouble.setText(String.valueOf(stats.getInt("double")));
        tTriple.setText(String.valueOf(stats.getInt("triple")));
        tQuadra.setText(String.valueOf(stats.getInt("quadra")));
        tPenta.setText(String.valueOf(stats.getInt("penta")));
        tTurrets.setText(String.valueOf(stats.getInt("turrets")));

        tDealt.setText(String.format("%.0f", stats.getInt("dealt") / totalGames));
        tTaken.setText(String.format("%.0f", stats.getInt("taken") / totalGames));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);

        imageLoader = VolleyHelper.getInstance(this).getImageLoader();

        if (!isHeader) {
            loadNotHeaderView();
        }
        if (isHeader) {
            loadHeaderView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("champData", stats);
        outState.putBoolean("isHeader", isHeader);
    }

    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            stats = savedInstanceState.getBundle("champData");
            isHeader = savedInstanceState.getBoolean("isHeader", false);
        } else {
            Bundle extras = getIntent().getExtras();
            isHeader = extras.getBoolean("isHeader");
            stats = extras.getBundle("champData");
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
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, ActivityAbout.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
