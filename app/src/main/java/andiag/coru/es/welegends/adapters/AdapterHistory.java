package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.Match;
import andiag.coru.es.welegends.entities.Participant;
import andiag.coru.es.welegends.entities.ParticipantIdentities;
import andiag.coru.es.welegends.entities.ParticipantStats;
import andiag.coru.es.welegends.utils.static_data.DataHandler;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;

/**
 * Created by Andy on 26/06/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {

    private List<Match> historyList = new ArrayList<>();
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
    public void onBindViewHolder(HistoryViewHolder historyViewHolder, int i) {
        Match m = historyList.get(i);

        historyViewHolder.vMap.setText("MatchID "+m.getMatchId());


        historyViewHolder.position = i;
        getAllData(historyViewHolder,m);
        setAnimation(historyViewHolder.cardView, i);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // OUTSIDE METHODS

    public void updateHistory(List<Match> hL) {
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

        holder.vMap.setText(DataHandler.getMapName(mapid));
        holder.mapImage.setImageResource(ImagesHandler.getMap(mapid));
        holder.vChampName.setText(DataHandler.getChampName(champId));
        holder.vImageChamp.setImageResource(ImagesHandler.getChamp(champId));


        if (winner) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark));
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
        protected ImageView vImageChamp, mapImage;
        protected View view;
        protected LinearLayout linearLayout;
        protected CardView cardView;
        protected int position;


        public HistoryViewHolder(View v) {
            super(v);
            this.view = v;
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
            linearLayout = (LinearLayout) v.findViewById(R.id.layoutVD);
            mapImage = (ImageView) v.findViewById(R.id.card_background);
        }
    }
}
