package andiag.coru.es.welegends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.SuperActivities.AnimatedTabbedActivity;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.fragments.FragmentPlayerMatchDetails;
import andiag.coru.es.welegends.fragments.FragmentVictoryDefeatDetails;
import andiag.coru.es.welegends.utils.NetworkError;
import andiag.coru.es.welegends.utils.ViewServer;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

public class ActivityDetails extends AnimatedTabbedActivity {

    protected boolean isLoading = false;
    private ActivityDetails thisActivity;
    private long matchId;
    private String region;
    private Match match;
    private boolean isCreatingTabs = false;
    private String request;

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
        outState.putLong("matchId", matchId);
        outState.putString("region", region);
        outState.putSerializable("match", match);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            matchId = savedInstanceState.getLong("matchId");
            region = savedInstanceState.getString("region");
            match = (Match) savedInstanceState.getSerializable("match");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                matchId = extras.getLong("matchId");
                region = extras.getString("region");
                getMatchDetails();
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
            startActivity(new Intent(this, ActivityAbout.class));
        }

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMatchDataOnFragments() {
        //match data are here just set it on fragments
    }

    private void getMatchDetails() {
        if (isLoading) return;

        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        request = handler.getServer() + region.toLowerCase() + handler.getMatch() + matchId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        match = gson.fromJson(response.toString(), Match.class);
                        setMatchDataOnFragments();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(thisActivity, getString(NetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(thisActivity).getRequestQueue().add(jsonObjectRequest);
    }
}
