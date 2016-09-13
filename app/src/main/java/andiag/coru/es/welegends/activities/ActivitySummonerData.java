package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.fragments.BlankFragment;
import andiag.coru.es.welegends.fragments.FragmentNotifiable;
import andiag.coru.es.welegends.rest.entities.Summoner;

public class ActivitySummonerData extends ActivityAnimatedTabbed implements ActivityNotifiable<Summoner> {

    private final static String TAG = "ActivitySummonerData";
    public final static String ARG_SUMMONER = "SUMMONER";
    public final static String ARG_FRAGMENT = "FRAGMENT";

    private Summoner summoner;

    private boolean isCreatingTabs = false;
    private boolean isUnranked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_data);

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(ARG_FRAGMENT)) {
                ((FragmentNotifiable) ActivitySummoner.activity.getSupportFragmentManager()
                        .findFragmentByTag(getIntent().getStringExtra(ARG_FRAGMENT))).setActivityNotifiable(this);
            }
            this.summoner = (Summoner) getIntent().getSerializableExtra(ARG_SUMMONER);
            Log.d(TAG, "received: " + summoner.getId());
        } else {
            summoner= (Summoner) savedInstanceState.getSerializable(ARG_SUMMONER);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        createTabs();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_SUMMONER,summoner);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void notifyDataChange(Summoner newSummoner) {
        Log.d(TAG, "notifySummonerDataChange: " + summoner.getId());
        this.summoner = newSummoner;
        //TODO notify view
    }

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
            String tabName;

            //FRAGMENT PLAYER STATS
            tab = new Tab();
            if (summoner != null) {
                tabName = summoner.getName().toUpperCase();
            } else {
                tabName = getString(R.string.section_summoner).toUpperCase();
            }
            tab.setFragment(new BlankFragment());
            tab.setName(tabName.toUpperCase());
            tab.setActionBarColors(getResources().getColor(R.color.posT0));
            tab.setToolBarColors(getResources().getColor(R.color.pos0));

            tabs.add(pos, tab);
            pos++;

            if (summoner.getSummonerLevel() == 30 && !isUnranked) {
                //FRAGMENT RANKEDS TAB
                tab = new Tab();
                tab.setFragment(new BlankFragment());
                tab.setName(getString(R.string.section_ranked).toUpperCase());
                tab.setActionBarColors(getResources().getColor(R.color.posT1));
                tab.setToolBarColors(getResources().getColor(R.color.pos1));

                tabs.add(pos, tab);
                pos++;
            }

            //FRAGMENT HISTORY TAB
            tab = new Tab();
            tab.setFragment(new BlankFragment());
            tab.setName(getString(R.string.section_history).toUpperCase());
            tab.setActionBarColors(getResources().getColor(R.color.posT2));
            tab.setToolBarColors(getResources().getColor(R.color.pos2));

            tabs.add(pos, tab);
            pos++;

            if (summoner.getSummonerLevel() == 30 && !isUnranked) {
                //FRAGMENT CHAMPIONS STATS
                tab = new Tab();
                tab.setFragment(new BlankFragment());
                tab.setName(getString(R.string.section_champs).toUpperCase());
                tab.setActionBarColors(getResources().getColor(R.color.posT3));
                tab.setToolBarColors(getResources().getColor(R.color.pos3));

                tabs.add(pos, tab);
            }

            setPager();
            setCreatingTabs(false);
        }
    }

}
