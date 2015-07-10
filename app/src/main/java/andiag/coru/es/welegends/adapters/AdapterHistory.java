package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantIdentities;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;
import andiag.coru.es.welegends.utils.static_data.NamesHandler;

/**
 * Created by Andy on 26/06/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {

    private List<Bundle> historyList = new ArrayList<>();
    private Context context;
    private DateFormat dateF;
    private int lastPosition = -1;
    private long sum_id;

    public AdapterHistory(Context context,long sum_id) {
        this.context = context;
        this.sum_id = sum_id;
        dateF = DateFormat.getDateInstance(DateFormat.SHORT,context.getResources().getConfiguration().locale);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_history_list, viewGroup, false);

        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int i) {
        Bundle bundle = historyList.get(i);

                holder.position = i;
        holder.vCS.setText(bundle.getString("cs"));
        holder.vGold.setText(bundle.getString("gold"));
        holder.vLVL.setText(bundle.getString("lvl"));
        holder.vKDA.setText(bundle.getString("kda"));
        holder.vDuration.setText(bundle.getString("duration"));
        holder.vMap.setText(bundle.getInt("mapName"));
        holder.relativeImage.setBackgroundResource(bundle.getInt("mapImage"));
        holder.vChampName.setText(bundle.getString("champName"));
        holder.vImageChamp.setImageResource(bundle.getInt("champImage"));
        if (bundle.getBoolean("winner")) {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.win));
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.lose));
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // OUTSIDE METHODS

    public void updateHistory(List<Bundle> hL) {
        if (hL != null) {
            historyList.addAll(hL);
            notifyDataSetChanged();
        }
    }

    public void clearHistory() {
        historyList.clear();
        notifyDataSetChanged();
    }

    // GET DATA

    private void getAllData(HistoryViewHolder holder,Match m) {


        int mapid = m.getMapId(),champId=0;
        long creation = m.getMatchCreation();
        long duration = m.getMatchDuration();
        long kills=0,assists=0,deaths=0,minions=0,lvl=0,gold=0;
        boolean winner = false;

        long participantId = -1;
        for (ParticipantIdentities pi : m.getParticipantIdentities()) {
            if (pi.getPlayer().getSummonerId() == sum_id) {
                participantId = pi.getParticipantId();
                break;
            }
        }

        if (participantId < 0) return;

        for (Participant p : m.getParticipants()) {
            if (p.getParticipantId() == participantId) {
                ParticipantStats stats = p.getStats();
                champId=p.getChampionId();
                kills=stats.getKills();
                deaths=stats.getDeaths();
                assists=stats.getAssists();
                lvl=stats.getChampLevel();
                minions=stats.getMinionsKilled();
                winner=stats.isWinner();
                gold=stats.getGoldEarned();
                break;
            }
        }

        holder.vCS.setText(Long.toString(minions));
        holder.vGold.setText(String.format("%.1f",(float) gold/1000)+"k");
        holder.vLVL.setText(Long.toString(lvl));
        holder.vKDA.setText(kills + "/" + deaths + "/" + assists);

        String d = String.format("%d ' %d ''",
                TimeUnit.SECONDS.toMinutes(duration),
                duration -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(duration))
        );
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(creation);
        String date_s = dateF.format(date.getTime());

        holder.vDuration.setText(date_s + "   " + d);

        holder.vMap.setText(NamesHandler.getMapName(mapid));
        holder.relativeImage.setBackgroundResource(ImagesHandler.getMap(mapid));
        holder.vChampName.setText(NamesHandler.getChampName(champId));
        holder.vImageChamp.setImageResource(ImagesHandler.getChamp(champId));


        if (winner) {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.win));
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.lose));
        }


    }

    // ANIMATION METHOD

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    // VIEWHOLDER CLASS

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView vChampName;
        protected TextView vMap, vDuration;
        protected TextView vKDA, vLVL, vCS, vGold;
        protected ImageView vImageChamp;
        protected View view;
        protected RelativeLayout relativeLayout,relativeImage;
        protected CardView cardView;
        protected int position;


        public HistoryViewHolder(View v) {
            super(v);
            this.view = v;
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ActivityDetails.class);
                    context.startActivity(i);
                }
            });

            vChampName = (TextView) v.findViewById(R.id.textSummonerName);
            vMap = (TextView) v.findViewById(R.id.textMap);
            vKDA = (TextView) v.findViewById(R.id.textKDA);
            vCS = (TextView) v.findViewById(R.id.textCS);
            vLVL = (TextView) v.findViewById(R.id.textLVL);
            vGold = (TextView) v.findViewById(R.id.textGold);
            vDuration = (TextView) v.findViewById(R.id.textDuration);
            vImageChamp = (ImageView) v.findViewById(R.id.imgChamp);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.RelativeLayoutText);
            relativeImage = (RelativeLayout) v.findViewById(R.id.RelativeLayoutImage);
        }
    }





}
