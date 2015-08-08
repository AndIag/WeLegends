package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.SuperActivities.AnimatedTabbedActivity;
import andiag.coru.es.welegends.fragments.FragmentPlayerMatchDetails;
import andiag.coru.es.welegends.fragments.FragmentVictoryDefeatDetails;
import andiag.coru.es.welegends.utils.ViewServer;

public class ActivityDetails extends AnimatedTabbedActivity {

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

            //PLAYER STATS
            tab = new Tab();
            tab.setFragment(FragmentPlayerMatchDetails.newInstance());
            tab.setName(getString(R.string.title_section_champion));
            tab.setActionBarColors(getResources().getColor(R.color.posT0));
            tab.setToolBarColors(getResources().getColor(R.color.posT0));

            tabs.add(pos, tab);
            pos++;

            //VICTORY TEAM
            tab = new Tab();
            tab.setFragment(FragmentVictoryDefeatDetails.newInstance());
            tab.setName(getString(R.string.title_section_victory_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT2));
            tab.setToolBarColors(getResources().getColor(R.color.posT2));

            tabs.add(pos, tab);
            pos++;

            //DEFEAT TEAM
            tab = new Tab();
            tab.setFragment(FragmentVictoryDefeatDetails.newInstance());
            tab.setName(getString(R.string.title_section_defeat_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT4));
            tab.setToolBarColors(getResources().getColor(R.color.posT4));

            tabs.add(pos, tab);

            setPager();
            setCreatingTabs(false);
        }
    }

    //SaveData
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NEED THIS TO USE SETANIMATION METHOD
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //GETTING DATA AND SET TABS TO VIEW PAGER
        onRetrieveInstanceState(savedInstanceState);
        createTabs();

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
        this.finish();
    }

}
