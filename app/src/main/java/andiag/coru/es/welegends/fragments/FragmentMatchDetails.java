package andiag.coru.es.welegends.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.concurrent.TimeUnit;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMatchDetails;
import andiag.coru.es.welegends.utils.API;
import andiag.coru.es.welegends.utils.StatsColor;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.Champions;
import andiag.coru.es.welegends.utils.static_data.Spells;

/*
            String  "version"
            Int     "championId"
            Long    "duration"
            String  "role"
            String  "lane"
            Int     "spell1"
            Int     "spell2"
            Long    "kills"
            Long    "deaths"
            Long    "assists"
            Long    "cs"
            Long    "gold"
            Long    "item0"
            Long    "item1"
            Long    "item2"
            Long    "item3"
            Long    "item4"
            Long    "item5"
            Long    "item6"
            Long    "damageDealt"
            Long    "damageTaken"
            Long    "healDone"
            Long    "physicalDamageDealt"
            Long    "magicDamageDealt"
            Long    "killingSprees"
*/
public class FragmentMatchDetails extends SwipeRefreshLayoutFragment implements NotifycableFragment {

    private static ActivityMatchDetails activityMain;
    private TextView textK, textD, textA, textKDA, textCS, textGold, textGoldPerMin, textCsPerMin, textChampName;
    private TextView textDamageDealt, textDamageTaken, textHeal, textPhyDealt, textMagDealt, textKillingSpree, textRole, textDuration;
    private NetworkImageView imgTotem, imgIt1, imgIt2, imgIt3, imgIt4, imgIt5, imgIt6, imageChampion, imageSpell, imageSpell1;
    private ImageView imageRole;
    private Bundle data;
    private ImageLoader imageLoader;
    private String version;

    public FragmentMatchDetails() {
        // Required empty public constructor
    }

    public static FragmentMatchDetails newInstance() {
        return new FragmentMatchDetails();
    }

    @Override
    public void notifyFragment() {
        data = activityMain.getSummonerData();
        if (isAdded() && data != null) {
            setDataOnView();
        } else {
            if (isAdded())
                Toast.makeText(activityMain, getString(R.string.unknowkError), Toast.LENGTH_LONG).show();
        }
    }

    private void setDataOnView() {
        //Get the server version for necesary for the image resources
        String badVersion = data.getString("version");
        if (badVersion != null) {
            String[] parts = badVersion.split("\\.");
            if (parts.length >= 2) {
                version = parts[0] + "." + parts[1] + ".1";
            } else {
                version = Champions.getServerVersion();
            }
        } else {
            version = Champions.getServerVersion();
        }

        long duration = data.getLong("duration");
        textDuration.setText(String.format("%d' %d''",
                TimeUnit.SECONDS.toMinutes(duration),
                duration - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))));

        textRole.setText(activityMain.getString(getNameToRole(getRole(data.getString("role"), data.getString("lane")))));
        imageRole.setImageResource(getImageToRole(getRole(data.getString("role"), data.getString("lane"))));

        textChampName.setText(Champions.getChampName(data.getInt("championId")));
        if (activityMain.isWinner()) {
            textChampName.setBackgroundResource(R.color.win);
        } else {
            textChampName.setBackgroundResource(R.color.lose);
        }

        long deaths = data.getLong("deaths");
        if (deaths == 0) deaths = 1;
        float kda = ((float) data.getLong("kills") + (float) data.getLong("assists")) / deaths;
        textKDA.setText(String.format("%.1f", kda));
        textKDA.setTextColor(activityMain.getResources().getColor(StatsColor.getKDAColor(kda)));

        textK.setText(String.valueOf(data.getLong("kills")));
        textK.setTextColor(activityMain.getResources().getColor(StatsColor.getKDAColor(kda)));

        textD.setText(String.valueOf(data.getLong("deaths")));
        textD.setTextColor(activityMain.getResources().getColor(StatsColor.getDeathColor(data.getLong("deaths"), duration)));

        textA.setText(String.valueOf(data.getLong("assists")));
        textA.setTextColor(activityMain.getResources().getColor(StatsColor.getKDAColor(kda)));

        textCS.setText(String.valueOf(data.getLong("cs")));
        textCS.setTextColor(activityMain.getResources().getColor(StatsColor.getCSColor(data.getLong("cs"),
                duration, getRole(data.getString("role"), data.getString("lane")))));

        textGold.setText(String.format("%.1f", ((float) data.getLong("gold")) / 1000) + "k");
        textGold.setTextColor(activityMain.getResources().getColor(StatsColor.getGoldColor(data.getLong("gold"),
                duration, getRole(data.getString("role"), data.getString("lane")))));

        imageChampion.setErrorImageResId(R.drawable.default_champion_error);
        imageChampion.setDefaultImageResId(R.drawable.default_champion);
        imageChampion.setImageUrl(API.getChampionIcon(Champions.getChampKey(data.getInt("championId"))),
                imageLoader);

        setItemImage(imgTotem, data.getLong("item6"));
        setItemImage(imgIt1, data.getLong("item0"));
        setItemImage(imgIt2, data.getLong("item1"));
        setItemImage(imgIt3, data.getLong("item2"));
        setItemImage(imgIt4, data.getLong("item3"));
        setItemImage(imgIt5, data.getLong("item4"));
        setItemImage(imgIt6, data.getLong("item5"));

        imageSpell.setErrorImageResId(R.drawable.default_champion_error);
        imageSpell.setDefaultImageResId(R.drawable.default_champion);
        imageSpell.setImageUrl(API.getSummonerSpellImage(Spells.getServerVersion()
                , Spells.getSpellKey(data.getInt("spell1"))), imageLoader);

        imageSpell1.setErrorImageResId(R.drawable.default_champion_error);
        imageSpell1.setDefaultImageResId(R.drawable.default_champion);
        imageSpell1.setImageUrl(API.getSummonerSpellImage(Spells.getServerVersion()
                , Spells.getSpellKey(data.getInt("spell2"))), imageLoader);

        float minDuration = ((float) data.getLong("duration")) / 60;
        textGoldPerMin.setText(String.format("%.1f", ((float) data.getLong("gold")) / minDuration));
        textGoldPerMin.setTextColor(activityMain.getResources().getColor(StatsColor.getGoldColor(data.getLong("gold"),
                duration, getRole(data.getString("role"), data.getString("lane")))));
        textCsPerMin.setText(String.format("%.1f", ((float) data.getLong("cs")) / minDuration));
        textCsPerMin.setTextColor(activityMain.getResources().getColor(StatsColor.getCSColor(data.getLong("cs"),
                duration, getRole(data.getString("role"), data.getString("lane")))));
        textDamageTaken.setText(String.valueOf(data.getLong("damageTaken")));
        textDamageDealt.setText(String.valueOf(data.getLong("damageDealt")));
        textHeal.setText(String.valueOf(data.getLong("healDone")));
        textPhyDealt.setText(String.valueOf(data.getLong("physicalDamageDealt")));
        textMagDealt.setText(String.valueOf(data.getLong("magicDamageDealt")));
        textKillingSpree.setText(String.valueOf(data.getLong("killingSprees")));

        changeRefreshingValue(false);
    }

    private void setItemImage(NetworkImageView imgView, long id) {
        imgView.setDefaultImageResId(R.drawable.default_item);
        if (id > 0) {
            imgView.setErrorImageResId(R.drawable.default_item_error);
            imgView.setDefaultImageResId(R.drawable.default_item);
            imgView.setImageUrl(API.getItemImage(version, id),
                    imageLoader);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityMatchDetails) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        data = activityMain.getSummonerData();
        if (data != null) {
            setDataOnView();
        }
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changeRefreshingValue(false);
            }
        });
        changeRefreshingValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = activityMain.getLayoutInflater();

        View view = lf.inflate(R.layout.fragment_match_detail, container, false);

        initializeRefresh(view);

        textChampName = (TextView) view.findViewById(R.id.textChampName);
        textRole = (TextView) view.findViewById(R.id.textRole);
        imageRole = (ImageView) view.findViewById(R.id.imageRole);
        textK = (TextView) view.findViewById(R.id.textK);
        textD = (TextView) view.findViewById(R.id.textD);
        textA = (TextView) view.findViewById(R.id.textA);
        textKDA = (TextView) view.findViewById(R.id.textKDA);
        textCS = (TextView) view.findViewById(R.id.textCS);
        textGold = (TextView) view.findViewById(R.id.textGold);
        textDuration = (TextView) view.findViewById(R.id.textDuration);
        imageChampion = (NetworkImageView) view.findViewById(R.id.imageChampion);
        imgTotem = (NetworkImageView) view.findViewById(R.id.imageTotem);
        imgIt1 = (NetworkImageView) view.findViewById(R.id.imageItem1);
        imgIt2 = (NetworkImageView) view.findViewById(R.id.imageItem2);
        imgIt3 = (NetworkImageView) view.findViewById(R.id.imageItem3);
        imgIt4 = (NetworkImageView) view.findViewById(R.id.imageItem4);
        imgIt5 = (NetworkImageView) view.findViewById(R.id.imageItem5);
        imgIt6 = (NetworkImageView) view.findViewById(R.id.imageItem6);
        textGoldPerMin = (TextView) view.findViewById(R.id.textGoldPerMinute);
        textCsPerMin = (TextView) view.findViewById(R.id.textCSperMinute);
        textDamageDealt = (TextView) view.findViewById(R.id.textTotalDamageDealt);
        textDamageTaken = (TextView) view.findViewById(R.id.textTotalDamageTaken);
        textHeal = (TextView) view.findViewById(R.id.textHealth);
        textPhyDealt = (TextView) view.findViewById(R.id.textPhysicalDamageDealt);
        textMagDealt = (TextView) view.findViewById(R.id.textMagicDamageDealt);
        textKillingSpree = (TextView) view.findViewById(R.id.textKillingSprees);
        imageSpell = (NetworkImageView) view.findViewById((R.id.imageSpell1));
        imageSpell1 = (NetworkImageView) view.findViewById((R.id.imageSpell2));

        return view;
    }

    private int getRole(String role, String lane) {
        //Participant's lane (Legal values: MID, MIDDLE, TOP, JUNGLE, BOT, BOTTOM)
        //Participant's role (Legal values: DUO, NONE, SOLO, DUO_CARRY, DUO_SUPPORT)

        if (lane.equals("JUNGLE")) return 0;
        if (lane.contains("MID")) return 1;
        if (lane.equals("TOP")) return 2;
        if (lane.contains("BOT") && role.contains("CARRY")) return 3;
        if (lane.contains("BOT") && role.contains("SUPPORT")) return 4;
        if (lane.contains("BOT")) return 5;
        return -1;
    }

    private int getNameToRole(int role) {
        switch (role) {
            case 0:
                return R.string.jungle;
            case 1:
                return R.string.mid;
            case 2:
                return R.string.top;
            case 3:
                return R.string.carry;
            case 4:
                return R.string.support;
            case 5:
                return R.string.bot;
            default:
                return R.string.error404;
        }
    }

    private int getImageToRole(int role) {
        switch (role) {
            case 0:
                return R.drawable.role_fighter;
            case 1:
                return R.drawable.role_mage;
            case 2:
                return R.drawable.role_tank;
            case 3:
                return R.drawable.role_marksman;
            case 4:
                return R.drawable.role_support;
            case 5:
                return R.drawable.role_assassin;
            default:
                return R.drawable.default_champion_error;
        }
    }

}