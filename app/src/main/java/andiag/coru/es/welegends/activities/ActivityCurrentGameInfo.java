package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.SuperActivities.AnimatedTabbedActivity;
import andiag.coru.es.welegends.entities.DTOs.currentGame.CurrentGameInfo;
import andiag.coru.es.welegends.fragments.FragmentCurrentGameTeam;

/**
 * Created by iagoc on 03/09/2015.
 */
public class ActivityCurrentGameInfo extends AnimatedTabbedActivity {

    private ActivityCurrentGameInfo thisActivity;

    private CurrentGameInfo currentGameInfo;
    private long summonerId;

    private boolean isCreatingTabs = false;

    private synchronized void setCreatingTabs(boolean bool) {
        isCreatingTabs = bool;
    }

    @Override
    protected void createTabs() {
        if (!isCreatingTabs) {
            setCreatingTabs(true);
            super.createTabs();
            int pos = 0;
            Tab tab;

            tab = new Tab();
            tab.setFragment(FragmentCurrentGameTeam.newInstance(true));
            tab.setName(getString(R.string.title_section_my_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT0));
            tab.setToolBarColors(getResources().getColor(R.color.pos0));

            tabs.add(pos, tab);
            pos++;

            tab = new Tab();
            tab.setFragment(FragmentCurrentGameTeam.newInstance(false));
            tab.setName(getString(R.string.title_section_enemy_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT1));
            tab.setToolBarColors(getResources().getColor(R.color.pos1));

            tabs.add(pos, tab);

            setPager();
            setCreatingTabs(false);
        }
    }

    //SaveData
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("currentGame", currentGameInfo);
        outState.putLong("summonerId", summonerId);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentGameInfo = (CurrentGameInfo) savedInstanceState.getSerializable("currentGame");
            summonerId = savedInstanceState.getLong("summonerId");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                currentGameInfo = (CurrentGameInfo) extras.getSerializable("currentGame");
                summonerId = extras.getLong("summonerId");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisActivity = this;

        //NEED THIS TO USE SETANIMATION METHOD
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //GETTING DATA AND SET TABS TO VIEW PAGER
        onRetrieveInstanceState(savedInstanceState);
        createTabs();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, ActivityAbout.class));
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
