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

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.SuperActivities.AnimatedTabbedActivity;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.entities.Team;
import andiag.coru.es.welegends.fragments.FragmentMatchDetails;
import andiag.coru.es.welegends.fragments.FragmentVictoryDefeatDetails;
import andiag.coru.es.welegends.fragments.NotifycableFragment;
import andiag.coru.es.welegends.utils.API;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

public class ActivityMatchDetails extends AnimatedTabbedActivity {

    protected boolean isLoading = false;
    private ActivityMatchDetails thisActivity;
    private long matchId;
    private int principalChampId;
    private int previousPrincipalChampId = -1;
    private boolean isWinner, isRanked = false;
    private boolean previousIsWinner;
    private String region;
    private Match match;
    private boolean isCreatingTabs = false;

    private synchronized void setCreatingTabs(boolean bool) {
        isCreatingTabs = bool;
    }

    public long getMatchId() {
        return matchId;
    }

    public String getRegion() {
        return region;
    }

    public Match getMatch() {
        return match;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public int getPrincipalChampId() {
        return principalChampId;
    }

    public boolean isRanked() {
        return isRanked;
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
            tab.setFragment(FragmentMatchDetails.newInstance());
            tab.setName(getString(R.string.title_section_champion));
            tab.setActionBarColors(getResources().getColor(R.color.posT0));
            tab.setToolBarColors(getResources().getColor(R.color.pos0));

            tabs.add(pos, tab);
            pos++;

            //VICTORY TEAM
            tab = new Tab();
            tab.setFragment(FragmentVictoryDefeatDetails.newInstance(true));
            tab.setName(getString(R.string.title_section_victory_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT2));
            tab.setToolBarColors(getResources().getColor(R.color.pos2));

            tabs.add(pos, tab);
            pos++;

            //DEFEAT TEAM
            tab = new Tab();
            tab.setFragment(FragmentVictoryDefeatDetails.newInstance(false));
            tab.setName(getString(R.string.title_section_defeat_team));
            tab.setActionBarColors(getResources().getColor(R.color.posT4));
            tab.setToolBarColors(getResources().getColor(R.color.pos4));

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
        outState.putInt("principalChamp", principalChampId);
        outState.putBoolean("isWinner", isWinner);
        outState.putBoolean("isRanked", isRanked);
        outState.putSerializable("match", match);
        outState.putBoolean("prevIsWinner", previousIsWinner);
        outState.putInt("prevPrincipalChamp", previousPrincipalChampId);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            matchId = savedInstanceState.getLong("matchId");
            region = savedInstanceState.getString("region");
            principalChampId = savedInstanceState.getInt("principalChamp");
            isWinner = savedInstanceState.getBoolean("isWinner");
            isRanked = savedInstanceState.getBoolean("isRanked");
            match = (Match) savedInstanceState.getSerializable("match");
            previousIsWinner = savedInstanceState.getBoolean("prevIsWinner");
            previousPrincipalChampId = savedInstanceState.getInt("prevPrincipalChamp");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                matchId = extras.getLong("matchId");
                region = extras.getString("region");
                principalChampId = extras.getInt("principalChamp");
                isWinner = extras.getBoolean("isWinner");
                isRanked = extras.getBoolean("isRanked", false);
                if (extras.containsKey("match")) {
                    match = (Match) extras.getSerializable("match");
                    previousIsWinner = extras.getBoolean("prevIsWinner");
                    previousPrincipalChampId = extras.getInt("prevPrincipalChamp");
                    notifyFragments();
                } else {
                    getMatchDetails();
                }
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
        if (previousPrincipalChampId >= 0) {
            Intent i = new Intent(this, ActivityMatchDetails.class);
            i.putExtra("region", region);
            i.putExtra("matchId", matchId);
            i.putExtra("principalChamp", previousPrincipalChampId);
            i.putExtra("isWinner", previousIsWinner);
            i.putExtra("match", match);
            i.putExtra("prevIsWinner", true);
            i.putExtra("prevPrincipalChamp", -1);
            startActivity(i);
        }
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

    public synchronized ArrayList<Bundle> getWinnerTeam() {
        if (match == null) return null;

        ArrayList<Bundle> list = new ArrayList<>();
        Bundle b;
        int kills = 0, deaths = 0, assists = 0;

        for (Participant p : match.getParticipants()) {
            if (p.getStats().isWinner()) {
                b = new Bundle();
                b.putSerializable("participant", p);
                kills += p.getStats().getKills();
                deaths += p.getStats().getDeaths();
                assists += p.getStats().getAssists();
                list.add(b);
            }
        }

        b = new Bundle();

        if (match.getTeams() != null) {
            for (Team t : match.getTeams()) {
                if (t.isWinner()) {
                    b.putSerializable("team", t);
                    break;
                }
            }
        }

        b.putString("type", match.getQueueType());
        b.putInt("totalKills", kills);
        b.putInt("totalDeaths", deaths);
        b.putInt("totalAssists", assists);


        list.add(0, b);
        return list;
    }

    public synchronized ArrayList<Bundle> getLosserTeam() {
        if (match == null) return null;

        ArrayList<Bundle> list = new ArrayList<>();
        Bundle b;
        int kills = 0, deaths = 0, assists = 0;

        for (Participant p : match.getParticipants()) {
            if (!p.getStats().isWinner()) {
                b = new Bundle();
                b.putSerializable("participant", p);
                kills += p.getStats().getKills();
                deaths += p.getStats().getDeaths();
                assists += p.getStats().getAssists();
                list.add(b);
            }
        }

        b = new Bundle();

        if (match.getTeams() != null) {
            for (Team t : match.getTeams()) {
                if (!t.isWinner()) {
                    b.putSerializable("team", t);
                    break;
                }
            }
        }

        b.putString("type", match.getQueueType());
        b.putInt("totalKills", kills);
        b.putInt("totalDeaths", deaths);
        b.putInt("totalAssists", assists);


        list.add(0, b);
        return list;
    }

    public synchronized Bundle getSummonerData() {
        if (match == null) return null;

        Bundle b = null;
        Participant participant = null;
        ParticipantStats participantStats;

        for (Participant p : match.getParticipants()) {
            if (!isRanked) {
                if (p.getChampionId() == principalChampId && p.getStats().isWinner() == isWinner) {
                    participant = p;
                    break;
                }
            } else {
                if (p.getChampionId() == principalChampId) {
                    participant = p;
                    isWinner = p.getStats().isWinner();
                    break;
                }
            }
        }

        if (participant != null) {
            b = new Bundle();
            participantStats = participant.getStats();
            b.putString("version", match.getMatchVersion());
            b.putInt("championId", participant.getChampionId());
            b.putString("role", participant.getTimeline().getRole());
            b.putString("lane", participant.getTimeline().getLane());
            b.putLong("duration", match.getMatchDuration());
            b.putInt("spell1", participant.getSpell1Id());
            b.putInt("spell2", participant.getSpell2Id());
            b.putLong("kills", participantStats.getKills());
            b.putLong("deaths", participantStats.getDeaths());
            b.putLong("assists", participantStats.getAssists());
            b.putLong("cs", participantStats.getMinionsKilled() + participantStats.getNeutralMinionsKilled());
            b.putLong("gold", participantStats.getGoldEarned());
            b.putLong("item0", participantStats.getItem0());
            b.putLong("item1", participantStats.getItem1());
            b.putLong("item2", participantStats.getItem2());
            b.putLong("item3", participantStats.getItem3());
            b.putLong("item4", participantStats.getItem4());
            b.putLong("item5", participantStats.getItem5());
            b.putLong("item6", participantStats.getItem6());
            b.putLong("killingSprees", participantStats.getLargestKillingSpree());
            b.putLong("damageDealt", participantStats.getTotalDamageDealtToChampions());
            b.putLong("damageTaken", participantStats.getTotalDamageTaken());
            b.putLong("physicalDamageDealt", participantStats.getPhysicalDamageDealtToChampions());
            b.putLong("magicDamageDealt", participantStats.getMagicDamageDealtToChampions());
            b.putLong("healDone", participantStats.getTotalHeal());
        }
        return b;
    }

    private void notifyFragments() {
        for (Tab t : tabs) {
            ((NotifycableFragment) t.getFragment()).notifyFragment();
        }
    }

    private void getMatchDetails() {
        if (isLoading) return;

        final Gson gson = new Gson();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                API.getMatch(region.toLowerCase(), matchId), (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        match = gson.fromJson(response.toString(), Match.class);
                        notifyFragments();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(thisActivity, getString(MyNetworkError.parseVolleyError(error)), Toast.LENGTH_LONG).show();
                thisActivity.finish();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyHelper.getInstance(thisActivity).getRequestQueue().add(jsonObjectRequest);
    }
}