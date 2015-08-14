package andiag.coru.es.welegends.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.BannedChampion;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;

/*
        haveTeams       Boolean
            baron       Int
            drake       Int
            haveBans    Boolean
                0       BannedChampion
                1       BannedChampion
                2       BannedChampion
        totalKills  Int
        totalDeaths Int
        totalAssits Int
    participant         Participant
 */

public class AdapterTeamDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Bundle> teamMembers = new ArrayList<>();
    private ImageLoader imageLoader;
    private boolean type;

    public AdapterTeamDetails(Context context,boolean type) {
        this.type = type;
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
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_participant, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bundle item = getItem(position);

        if (holder instanceof VHItem) {
            VHItem h = (VHItem) holder;
            Participant p = (Participant) item.getSerializable("participant");
            if (p != null) {
                ParticipantStats participantStats = p.getStats();
                h.textKDA.setText(participantStats.getKills() + "/"
                        + participantStats.getDeaths() + "/"
                        + participantStats.getAssists());
                h.textGold.setText(String.format("%.1f", (float) participantStats.getGoldEarned() / 1000) + "k");
                h.textCS.setText(String.valueOf(participantStats.getMinionsKilled() + participantStats.getNeutralMinionsKilled()) + "cs");
                h.textName.setText(ChampionsHandler.getChampName(p.getChampionId()));

                h.imageChamp.setErrorImageResId(R.drawable.default_champion);
                h.imageChamp.setDefaultImageResId(R.drawable.default_champion);
                h.imageChamp.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                                + ChampionsHandler.getServerVersion((Activity) context)
                                + "/img/champion/" + ChampionsHandler.getChampKey(p.getChampionId()) + ".png",
                        imageLoader);

                h.imageSpell1.setImageResource(ImagesHandler.getSpell(p.getSpell1Id()));
                h.imageSpell2.setImageResource(ImagesHandler.getSpell(p.getSpell2Id()));
            }
        } else if (holder instanceof VHHeader) {
            final VHHeader h = (VHHeader) holder;
            int color;
            if (type) {
                h.textBoolean.setText(context.getResources().getString(R.string.victory_team));
                color = context.getResources().getColor(R.color.win);
            } else {
                h.textBoolean.setText(context.getResources().getString(R.string.defeat_team));
                color = context.getResources().getColor(R.color.lose);

            }
            h.textKDA.setText(item.getInt("totalKills") + "/"
                    + item.getInt("totalDeaths") + "/" + item.getInt("totalAssits"));

            if (item.getBoolean("haveTeams", false)) {
                h.textBaron.setText(String.valueOf(item.getInt("baron")));
                h.textDragon.setText(String.valueOf(item.getInt("drake")));
                if (item.getBoolean("haveBans", false)) {
                    putBannedChampionOnView((BannedChampion) item.getSerializable("0"), h);
                    putBannedChampionOnView((BannedChampion) item.getSerializable("1"), h);
                    putBannedChampionOnView((BannedChampion) item.getSerializable("2"), h);
                }
            }

            h.textBoolean.setTextColor(color);
            h.imageBaron.setColorFilter(color);
            h.imageDragon.setColorFilter(color);

        }

    }

    private void putBannedChampionOnView(BannedChampion bannedChampion, VHHeader h) {
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 2 || bannedChampion.getPickTurn() == 1)) {
            h.textBanned1.setText(ChampionsHandler.getChampName(bannedChampion.getChampionId()));
            h.imageBanned1.setErrorImageResId(R.drawable.default_champion);
            h.imageBanned1.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned1.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + ChampionsHandler.getChampKey(bannedChampion.getChampionId()) + ".png",
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 4 || bannedChampion.getPickTurn() == 3)) {
            h.textBanned2.setText(ChampionsHandler.getChampName(bannedChampion.getChampionId()));
            h.imageBanned2.setErrorImageResId(R.drawable.default_champion);
            h.imageBanned2.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned2.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + ChampionsHandler.getChampKey(bannedChampion.getChampionId()) + ".png",
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 6 || bannedChampion.getPickTurn() == 5)) {
            h.textBanned3.setText(ChampionsHandler.getChampName(bannedChampion.getChampionId()));
            h.imageBanned3.setErrorImageResId(R.drawable.default_champion);
            h.imageBanned3.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned3.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + ChampionsHandler.getChampKey(bannedChampion.getChampionId()) + ".png",
                    imageLoader);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView textKDA,textGold,textCS,textName;
        NetworkImageView imageChamp;
        ImageView imageSpell1,imageSpell2;
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;
            textCS = (TextView) v.findViewById(R.id.textCS);
            textKDA = (TextView) v.findViewById(R.id.textKDA);
            textGold = (TextView) v.findViewById(R.id.textGold);
            textName = (TextView) v.findViewById(R.id.textName);
            imageChamp = (NetworkImageView) v.findViewById(R.id.imgChamp);
            imageSpell1 = (ImageView) v.findViewById(R.id.imageSpell1);
            imageSpell2 = (ImageView) v.findViewById(R.id.imageSpell2);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView textBoolean,textBanned1,textBanned2,textBanned3,textKDA,textBaron,textDragon,textName;
        NetworkImageView imageBanned1,imageBanned2,imageBanned3;
        ImageView imageDragon,imageBaron;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view=itemView;
            imageBanned1 = (NetworkImageView) view.findViewById(R.id.imageBanned1);
            imageBanned2 = (NetworkImageView) view.findViewById(R.id.imageBanned2);
            imageBanned3 = (NetworkImageView) view.findViewById(R.id.imageBanned3);
            textBoolean = (TextView) view.findViewById(R.id.textBoolean);
            textBanned1 = (TextView) view.findViewById(R.id.textBanned1);
            textBanned2 = (TextView) view.findViewById(R.id.textBanned2);
            textBanned3 = (TextView) view.findViewById(R.id.textBanned3);

            textKDA = (TextView) view.findViewById(R.id.textKDA);
            textBaron = (TextView) view.findViewById(R.id.textBaron);
            textDragon = (TextView) view.findViewById(R.id.textDragon);
            imageBaron = (ImageView) view.findViewById(R.id.imageBaron);
            imageDragon = (ImageView) view.findViewById(R.id.imageDragon);

        }
    }

}
