package andiag.coru.es.welegends.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.entities.BannedChampion;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.StatsColor;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;

/*
        haveTeams       Boolean
            baron       Int
            drake       Int
            vilemaw     Int  *Solo en 3x3
            haveBans    Boolean
                0       BannedChampion
                1       BannedChampion
                2       BannedChampion
        totalKills      Int
        totalDeaths     Int
        totalAssits     Int
        queue           String
    participant         Participant
 */

public class AdapterTeamDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Bundle> teamMembers = new ArrayList<>();
    private ImageLoader imageLoader;
    private boolean isWinner;

    public AdapterTeamDetails(Context context, boolean isWinner) {
        this.isWinner = isWinner;
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
                    inflate(R.layout.item_team_participant, parent, false);
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
                h.textCS.setText(String.valueOf(participantStats.getMinionsKilled() + participantStats.getNeutralMinionsKilled()));
                h.textName.setText(ChampionsHandler.getChampName(p.getChampionId()));

                h.imageChamp.setErrorImageResId(R.drawable.default_champion_error);
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
            if (isWinner) {
                h.textBoolean.setText(context.getResources().getString(R.string.victory_team));
                color = context.getResources().getColor(R.color.win);
            } else {
                h.textBoolean.setText(context.getResources().getString(R.string.defeat_team));
                color = context.getResources().getColor(R.color.lose);

            }
            h.textKDA.setText(item.getInt("totalKills") + "/"
                    + item.getInt("totalDeaths") + "/" + item.getInt("totalAssits"));

            float kda = ((float) item.getInt("totalKills") + (float) item.getInt("totalAssits"))
                    / (float) item.getInt("totalDeaths");
            h.textKDA.setTextColor(context.getResources().getColor(StatsColor.getColor(StatsColor.KDA, kda)));

            if (item.getBoolean("haveTeams", false)) {
                if (item.containsKey("vilemaw")) {
                    h.textBaron.setText(String.valueOf(item.getInt("vilemaw")));
                    h.imageBaron.setImageResource(R.drawable.vilemaw);
                    h.textDragon.setVisibility(View.GONE);
                    h.imageDragon.setVisibility(View.GONE);
                }
                h.textBaron.setText(String.valueOf(item.getInt("baron")));
                h.textDragon.setText(String.valueOf(item.getInt("drake")));
                if (item.getBoolean("haveBans", false)) {
                    putBannedChampionOnView((BannedChampion) item.getSerializable("0"), h);
                    putBannedChampionOnView((BannedChampion) item.getSerializable("1"), h);
                    putBannedChampionOnView((BannedChampion) item.getSerializable("2"), h);
                } else {
                    h.imageBanned1.setVisibility(View.GONE);
                    h.imageBanned2.setVisibility(View.GONE);
                    h.imageBanned3.setVisibility(View.GONE);
                    h.textBanned1.setVisibility(View.GONE);
                    h.textBanned2.setVisibility(View.GONE);
                    h.textBanned3.setVisibility(View.GONE);
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
            h.imageBanned1.setErrorImageResId(R.drawable.default_champion_error);
            h.imageBanned1.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned1.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + ChampionsHandler.getChampKey(bannedChampion.getChampionId()) + ".png",
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 4 || bannedChampion.getPickTurn() == 3)) {
            h.textBanned2.setText(ChampionsHandler.getChampName(bannedChampion.getChampionId()));
            h.imageBanned2.setErrorImageResId(R.drawable.default_champion_error);
            h.imageBanned2.setDefaultImageResId(R.drawable.default_champion);
            h.imageBanned2.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + ChampionsHandler.getChampKey(bannedChampion.getChampionId()) + ".png",
                    imageLoader);
        }
        if (bannedChampion != null && (bannedChampion.getPickTurn() == 6 || bannedChampion.getPickTurn() == 5)) {
            h.textBanned3.setText(ChampionsHandler.getChampName(bannedChampion.getChampionId()));
            h.imageBanned3.setErrorImageResId(R.drawable.default_champion_error);
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
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ActivityDetails.class);
                    Participant participant = (Participant) getItem(getAdapterPosition()).getSerializable("participant");
                    if (participant != null) {
                        i.putExtra("region", ((ActivityDetails) context).getRegion());
                        i.putExtra("matchId", ((ActivityDetails) context).getMatchId());
                        i.putExtra("principalChamp", participant.getChampionId());
                        i.putExtra("isWinner", participant.getStats().isWinner());
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                }
            });
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
        TextView textBoolean, textKDA, textBaron, textDragon;
        TextView textBanned1, textBanned2, textBanned3;
        CircledNetworkImageView imageBanned1, imageBanned2, imageBanned3;
        ImageView imageDragon, imageBaron;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view = itemView;
            textBoolean = (TextView) view.findViewById(R.id.textBoolean);
            textKDA = (TextView) view.findViewById(R.id.textKDA);
            textBaron = (TextView) view.findViewById(R.id.textBaron);
            textDragon = (TextView) view.findViewById(R.id.textDragon);
            imageBaron = (ImageView) view.findViewById(R.id.imageBaron);
            imageDragon = (ImageView) view.findViewById(R.id.imageDragon);
            imageBanned1 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned1);
            imageBanned2 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned2);
            imageBanned3 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned3);
            textBanned1 = (TextView) view.findViewById(R.id.textBanned1);
            textBanned2 = (TextView) view.findViewById(R.id.textBanned2);
            textBanned3 = (TextView) view.findViewById(R.id.textBanned3);
        }
    }
}
