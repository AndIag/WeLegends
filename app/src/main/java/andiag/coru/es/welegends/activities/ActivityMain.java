package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.SuperActivities.AnimatedTabbedActivity;
import andiag.coru.es.welegends.dialogs.DialogAbout;
import andiag.coru.es.welegends.entities.Summoner;
import andiag.coru.es.welegends.fragments.FragmentHistory;
import andiag.coru.es.welegends.fragments.FragmentPlayerStats;
import andiag.coru.es.welegends.utils.ViewServer;

public class ActivityMain extends AnimatedTabbedActivity {

    private Summoner summoner;
    private String summonerName;
    private String region;

    public String getRegion() {
        return region;
    }

    @Override
    protected void createTabs() {
        super.createTabs();
        Tab tab;
        String tabName;
        //FRAGMENT HISTORY TAB
        tab = new Tab();
        if (summoner != null) {
            tabName = summoner.getName().toUpperCase();
        } else if (summonerName != null) {
            tabName = summonerName.toUpperCase();
        } else {
            tabName = getString(R.string.title_section1).toUpperCase();
        }
        tab.setFragment(FragmentHistory.getInstance(this));
        tab.setName(tabName);
        tab.setActionBarColors(getResources().getColor(R.color.posT0));
        tab.setToolBarColors(getResources().getColor(R.color.posT0));

        tabs.add(0, tab);

        //FRAGMENT PLAYER STATS
        tab = new Tab();
        tab.setFragment(FragmentPlayerStats.getInstance(this));
        tab.setName(getString(R.string.title_section2).toUpperCase());
        tab.setActionBarColors(getResources().getColor(R.color.posT1));
        tab.setToolBarColors(getResources().getColor(R.color.posT1));

        tabs.add(1, tab);

        setPager();
    }

    //SaveData
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("summoner", summoner);
        outState.putString("region", region);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            summoner = (Summoner) savedInstanceState.getSerializable("summoner");
            region = savedInstanceState.getString("region");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                region = getIntent().getStringExtra("region");
                summoner = (Summoner) intent.getSerializableExtra("summoner");
                summonerName = summoner.getName();
            }
            ActivitySummoner.setActivityMain(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);

        //NEED THIS TO USE SETANIMATION METHOD
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //GETTING DATA AND SET TABS TO VIEW PAGER
        onRetrieveInstanceState(savedInstanceState);
        createTabs();

        //SETTING DATA IN FRAGMENTS
        FragmentHistory.getInstance(this).setSummoner_id(summoner.getId(), region);
        FragmentPlayerStats.getInstance(this).setSummoner(summoner);

        setAnimation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentHistory.deleteFragment();
        FragmentPlayerStats.deleteFragment();
        ActivitySummoner.setActivityMain(null);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogAbout dialogAbout = DialogAbout.newInstance();
            dialogAbout.show(getSupportFragmentManager(), "DialogAbout");
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
