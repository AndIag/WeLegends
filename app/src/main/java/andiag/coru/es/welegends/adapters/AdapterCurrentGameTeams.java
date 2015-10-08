package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.BannedChampion;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.handlers.API;
import andiag.coru.es.welegends.utils.handlers.Champions;
import andiag.coru.es.welegends.utils.handlers.Spells;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/*
        champId         int
        summonerId      long
        summonerName    string
        spell1          int
        spell2          int
        mastery         serializable
        runes           serializable


        teamId          long
        banned          serializable
 */
public class AdapterCurrentGameTeams extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Bundle> teamMembers = new ArrayList<>();
    private ImageLoader imageLoader;

    public AdapterCurrentGameTeams(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    public void updateTeamMembers(List<Bundle> cL) {
        if (cL != null) {
            this.teamMembers = cL;
            notifyDataSetChanged();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_current_participant, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("There is no isWinner that matches the isWinner " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == TYPE_HEADER;
    }

    private Bundle getItem(int position) {
        return teamMembers.get(position);
    }

    private void putBannedChampionOnView(BannedChampion bannedChampion, VHHeader h) {
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 2 || bannedChampion.getPickTurn() == 1)) {
            h.textBanned1.setText(Champions.getChampName(bannedChampion.getChampionId()));
            h.imageBanned1.setErrorImageResId(R.drawable.default_champion_error);
            h.imageBanned1.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned1.setImageUrl(API.getChampionIcon(Champions.getChampKey(bannedChampion.getChampionId())),
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 4 || bannedChampion.getPickTurn() == 3)) {
            h.textBanned2.setText(Champions.getChampName(bannedChampion.getChampionId()));
            h.imageBanned2.setErrorImageResId(R.drawable.default_champion_error);
            h.imageBanned2.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned2.setImageUrl(API.getChampionIcon(Champions.getChampKey(bannedChampion.getChampionId())),
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 6 || bannedChampion.getPickTurn() == 5)) {
            h.textBanned3.setText(Champions.getChampName(bannedChampion.getChampionId()));
            h.imageBanned3.setErrorImageResId(R.drawable.default_champion_error);
            h.imageBanned3.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned3.setImageUrl(API.getChampionIcon(Champions.getChampKey(bannedChampion.getChampionId())),
                    imageLoader);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bundle item = getItem(position);
        if (holder instanceof VHItem) {
            final VHItem h = (VHItem) holder;

            h.buttonM.setVisibility(View.INVISIBLE);
            h.buttonR.setVisibility(View.INVISIBLE);

            h.textSummName.setText(item.getString("summonerName"));
            h.textChampName.setText(Champions.getChampName(item.getInt("champId")));

            h.imageChamp.setErrorImageResId(R.drawable.default_champion_error);
            h.imageChamp.setDefaultImageResId(R.drawable.default_champion);
            h.imageChamp.setImageUrl(API.getChampionIcon(Champions.getChampKey(item.getInt("champId"))),
                    imageLoader);

            h.imageSpell1.setErrorImageResId(R.drawable.default_champion_error);
            h.imageSpell1.setDefaultImageResId(R.drawable.default_champion);
            h.imageSpell1.setImageUrl(API.getSummonerSpellImage(Spells.getServerVersion(), Spells.getSpellKey(item.getInt("spell1"))),
                    imageLoader);

            h.imageSpell2.setErrorImageResId(R.drawable.default_champion_error);
            h.imageSpell2.setDefaultImageResId(R.drawable.default_champion);
            h.imageSpell2.setImageUrl(API.getSummonerSpellImage(Spells.getServerVersion(), Spells.getSpellKey(item.getInt("spell2"))),
                    imageLoader);

        } else if (holder instanceof VHHeader) {
            final VHHeader h = (VHHeader) holder;

            h.imageDragon.setVisibility(View.GONE);
            h.imageBaron.setVisibility(View.GONE);
            h.textBoolean.setVisibility(View.GONE);

            if (item.containsKey("banned")) {
                ArrayList<BannedChampion> bans = (ArrayList<BannedChampion>) item.getSerializable("banned");
                if (bans != null) {
                    for (BannedChampion b : bans) {
                        if (b.getTeamId() == (int) item.getLong("teamId"))
                            putBannedChampionOnView(b, h);
                    }
                }
            } else {
                h.bans.setVisibility(View.GONE);
            }
        }

    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView textSummName, textChampName;
        NetworkImageView imageChamp, imageSpell1, imageSpell2;
        Button buttonR, buttonM;
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;

            textChampName = (TextView) v.findViewById(R.id.textName);
            textSummName = (TextView) v.findViewById(R.id.textSummonerName);

            imageChamp = (NetworkImageView) v.findViewById(R.id.imgChamp);
            imageSpell1 = (NetworkImageView) v.findViewById(R.id.imageSpell1);
            imageSpell2 = (NetworkImageView) v.findViewById(R.id.imageSpell2);

            buttonM = (Button) v.findViewById(R.id.buttonMasteries);
            buttonR = (Button) v.findViewById(R.id.buttonRunes);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView textBoolean;
        TextView textBanned1, textBanned2, textBanned3;
        CircledNetworkImageView imageBanned1, imageBanned2, imageBanned3;
        ImageView imageDragon, imageBaron;
        RelativeLayout bans;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view = itemView;
            bans = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            textBoolean = (TextView) view.findViewById(R.id.textBoolean);
            imageBaron = (ImageView) view.findViewById(R.id.imageBaron);
            imageDragon = (ImageView) view.findViewById(R.id.imageDragon);
            imageBanned1 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned1);
            imageBanned2 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned2);
            imageBanned3 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned3);
            textBanned1 = (TextView) view.findViewById(R.id.textBanned1);
            textBanned2 = (TextView) view.findViewById(R.id.textBanned2);
            textBanned3 = (TextView) view.findViewById(R.id.textBanned3);


            (view.findViewById(R.id.textKDA)).setVisibility(View.GONE);
            (view.findViewById(R.id.textBaron)).setVisibility(View.GONE);
            (view.findViewById(R.id.textDragon)).setVisibility(View.GONE);
            (view.findViewById(R.id.imageBaron)).setVisibility(View.GONE);
            (view.findViewById(R.id.imageDragon)).setVisibility(View.GONE);
        }
    }

}
