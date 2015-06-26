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
import java.util.List;
import java.util.Locale;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.Match;

/**
 * Created by Andy on 26/06/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {

    private List<Match> historyList = new ArrayList<>();
    private Context context;
    private DateFormat dateF;
    private int lastPosition = -1;

    public AdapterHistory(Context context) {
        this.context = context;
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
