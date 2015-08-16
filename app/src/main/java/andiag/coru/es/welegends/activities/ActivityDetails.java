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
import andiag.coru.es.welegends.entities.BannedChampion;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.entities.Team;
import andiag.coru.es.welegends.fragments.FragmentMatchDetails;
import andiag.coru.es.welegends.fragments.FragmentVictoryDefeatDetails;
import andiag.coru.es.welegends.fragments.NotifycableFragment;
import andiag.coru.es.welegends.utils.MyNetworkError;
import andiag.coru.es.welegends.utils.ViewServer;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;

public class ActivityDetails extends AnimatedTabbedActivity {

    protected boolean isLoading = false;
    private ActivityDetails thisActivity;
    private long matchId;
    private int principalChampId;
    private boolean isWinner;
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
        //super.onSaveInstanceState(outState);
        outState.putLong("matchId", matchId);
        outState.putString("region", region);
        outState.putInt("principalChamp", principalChampId);
        outState.putBoolean("isWinner", isWinner);
        outState.putSerializable("match", match);
    }

    //RetrieveData
    protected void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            matchId = savedInstanceState.getLong("matchId");
            region = savedInstanceState.getString("region");
            principalChampId = savedInstanceState.getInt("principalChamp");
            isWinner = savedInstanceState.getBoolean("isWinner");
            match = (Match) savedInstanceState.getSerializable("match");
        } else {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                matchId = extras.getLong("matchId");
                region = extras.getString("region");
                principalChampId = extras.getInt("principalChamp");
                isWinner = extras.getBoolean("isWinner");
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

        //if(savedInstanceState!=null) notifyFragments();

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

    private Bundle parseSummonerNormalData() {
        Bundle b = null;
        Participant participant = null;
        ParticipantStats participantStats;

        for (Participant p : match.getParticipants()) {
            if (p.getChampionId() == principalChampId && p.getStats().isWinner() == isWinner) {
                participant = p;
                break;
            }
        }

        if (participant != null) {
            b = new Bundle();
            participantStats = participant.getStats();
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

    private int matchType(String queurType) {
        //We might need this if some type of mach have different structure
        return 0;
    }

    private Bundle getDetailsData() {
        if (match == null) return null;
        switch (matchType(match.getQueueType())) {
            case 0:
                return parseSummonerNormalData();
        }
        return null;
    }

    private Bundle getWinnerTeamData() {
        if (match == null) return null;
        Bundle b = new Bundle();
        Bundle wTeam = new Bundle();
        Team winnerTeam = null;
        int kills = 0, assists = 0, deaths = 0, participant = 0;

        if (match.getTeams() != null) {
            for (Team t : match.getTeams()) {
                if (t.isWinner()) {
                    winnerTeam = t;
                    break;
                }
            }
            if (winnerTeam != null) {
                wTeam.putBoolean("haveTeams", true);
                if (match.getQueueType().contains("3x3")) {
                    wTeam.putInt("vilemaw", winnerTeam.getVilemawKills());
                } else {
                    wTeam.putInt("baron", winnerTeam.getBaronkills());
                    wTeam.putInt("drake", winnerTeam.getDragonkills());
                }
                if (winnerTeam.getBans() != null) {
                    wTeam.putBoolean("haveBans", true);
                    for (BannedChampion c : winnerTeam.getBans()) {
                        wTeam.putSerializable(String.valueOf(participant), c);
                        participant++;
                    }
                    participant = 0;
                }
            }
        }

        for (Participant p : match.getParticipants()) {
            if (p.getStats().isWinner()) {
                b.putSerializable(String.valueOf(participant), p);
                participant++;
                kills += p.getStats().getKills();
                deaths += p.getStats().getDeaths();
                assists += p.getStats().getAssists();
            }
        }

        wTeam.putInt("totalKills", kills);
        wTeam.putInt("totalDeaths", deaths);
        wTeam.putInt("totalAssits", assists);

        b.putBundle("team", wTeam);

        return b;
    }

    private Bundle getLosserTeamData() {
        if (match == null) return null;
        Bundle b = new Bundle();
        Bundle lTeam = new Bundle();
        Team losserTeam = null;
        int kills = 0, assists = 0, deaths = 0, participant = 0;

        if (match.getTeams() != null) {
            for (Team t : match.getTeams()) {
                if (!t.isWinner()) {
                    losserTeam = t;
                    break;
                }
            }
            if (losserTeam != null) {
                lTeam.putBoolean("haveTeams", true);
                if (match.getQueueType().contains("3x3")) {
                    lTeam.putInt("vilemaw", losserTeam.getVilemawKills());
                } else {
                    lTeam.putInt("baron", losserTeam.getBaronkills());
                    lTeam.putInt("drake", losserTeam.getDragonkills());
                }
                if (losserTeam.getBans() != null) {
                    lTeam.putBoolean("haveBans", true);
                    for (BannedChampion c : losserTeam.getBans()) {
                        lTeam.putSerializable(String.valueOf(participant), c);
                        participant++;
                    }
                    participant = 0;
                }
            }
        }

        for (Participant p : match.getParticipants()) {
            if (!p.getStats().isWinner()) {
                b.putSerializable(String.valueOf(participant), p);
                participant++;
                kills += p.getStats().getKills();
                deaths += p.getStats().getDeaths();
                assists += p.getStats().getAssists();
            }
        }

        lTeam.putInt("totalKills", kills);
        lTeam.putInt("totalDeaths", deaths);
        lTeam.putInt("totalAssits", assists);

        b.putBundle("team", lTeam);

        return b;
    }

    public synchronized Bundle getData(int fragment) {
        switch (fragment) {
            case 0:
                return getDetailsData();
            case 1:
                return getWinnerTeamData();
            case 2:
                return getLosserTeamData();
            default:
                return null;
        }
    }

    private void notifyFragments() {
        for (Tab t : tabs) {
            ((NotifycableFragment) t.getFragment()).notifyFragment();
        }
    }

    private void getMatchDetails() {
        if (isLoading) return;

        final Gson gson = new Gson();

        APIHandler handler = APIHandler.getInstance();
        if (handler == null) {
            handler = APIHandler.getInstance(this);
        }

        String request = handler.getServer() + region.toLowerCase() + handler.getMatch() + matchId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request, (String) null,
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