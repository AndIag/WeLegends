package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.activities.ActivityMatchDetails;
import andiag.coru.es.welegends.utils.handlers.API;
import andiag.coru.es.welegends.utils.handlers.Names;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by iagoc on 05/10/2015.
 */
public class AdapterRankedsHistory extends RecyclerView.Adapter<AdapterRankedsHistory.HistoryViewHolder> {


    private List<Bundle> historyList = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;

    public AdapterRankedsHistory(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_ranked_history, viewGroup, false);

        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int i) {
        Bundle bundle = historyList.get(i);

        holder.position = i;
        holder.vStartDate.setText(bundle.getString("startDate"));
        int matchMode = Names.getGameMode(bundle.getString("queueType"));
        if (matchMode >= 0) {
            holder.vMap.setText(context.getString(matchMode));
        } else {
            holder.vMap.setText(bundle.getString("queueType"));
        }
        holder.relativeImage.setBackgroundResource(bundle.getInt("mapImage"));
        holder.vChampName.setText(bundle.getString("champName"));
        holder.vImageChamp.setErrorImageResId(R.drawable.default_champion_error);
        holder.vImageChamp.setDefaultImageResId(R.drawable.default_champion);
        holder.vImageChamp.setImageUrl(API.getChampionIcon(bundle.getString("champKey")), imageLoader);

        holder.isRanked.setImageResource(bundle.getInt("matchType"));
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

    // VIEWHOLDER CLASS

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView vChampName;
        protected TextView vMap, vStartDate;
        protected ImageView isRanked;
        protected NetworkImageView vImageChamp;
        protected View view;
        protected RelativeLayout relativeLayout, relativeImage;
        protected CardView cardView;
        protected int position;


        public HistoryViewHolder(View v) {
            super(v);
            this.view = v;
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ActivityMatchDetails.class);
                    i.putExtra("matchId", (historyList.get(position).getLong("matchId")));
                    i.putExtra("region", ((ActivityMain) context).getRegion());
                    i.putExtra("principalChamp", historyList.get(position).getInt("champId"));
                    i.putExtra("isRanked", true);
                    context.startActivity(i);
                }
            });

            vChampName = (TextView) v.findViewById(R.id.textName);
            vMap = (TextView) v.findViewById(R.id.textMap);
            vStartDate = (TextView) v.findViewById(R.id.textStartDate);
            vImageChamp = (NetworkImageView) v.findViewById(R.id.imgChamp);
            isRanked = (ImageView) v.findViewById(R.id.isRanked);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.RelativeLayoutText);
            relativeImage = (RelativeLayout) v.findViewById(R.id.RelativeLayoutImage);
        }
    }

}
