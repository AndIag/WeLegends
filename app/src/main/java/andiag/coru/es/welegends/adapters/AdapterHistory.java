package andiag.coru.es.welegends.adapters;

import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by Andy on 26/06/2015.
 */
public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HistoryViewHolder> {

    private List<Bundle> historyList = new ArrayList<>();
    private Context context;
    private ImageLoader imageLoader;

    public AdapterHistory(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
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
        holder.vStartDate.setText(bundle.getString("startDate"));
        holder.vDuration.setText(bundle.getString("duration"));
        holder.vMap.setText(bundle.getInt("mapName"));
        holder.relativeImage.setBackgroundResource(bundle.getInt("mapImage"));
        holder.vChampName.setText(bundle.getString("champName"));
        holder.vImageChamp.setErrorImageResId(R.drawable.item_default);
        holder.vImageChamp.setDefaultImageResId(R.drawable.item_default);
        holder.vImageChamp.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                        + ChampionsHandler.getServerVersion((Activity) context)
                        + "/img/champion/" + bundle.getString("champKey") + ".png",
                imageLoader);

        holder.isRanked.setImageResource(bundle.getInt("isRanked"));
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

    // VIEWHOLDER CLASS

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView vChampName;
        protected TextView vMap, vDuration, vStartDate;
        protected TextView vKDA, vLVL, vCS, vGold;
        protected ImageView isRanked;
        protected NetworkImageView vImageChamp;
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
                    i.putExtra("matchId", (historyList.get(position).getLong("matchId")));
                    i.putExtra("region", ((ActivityMain) context).getRegion());
                    Toast.makeText(context, "CLICKED"
                            , Toast.LENGTH_LONG).show();
                    //context.startActivity(i);
                }
            });

            vChampName = (TextView) v.findViewById(R.id.textSummonerName);
            vMap = (TextView) v.findViewById(R.id.textMap);
            vKDA = (TextView) v.findViewById(R.id.textKDA);
            vCS = (TextView) v.findViewById(R.id.textCS);
            vLVL = (TextView) v.findViewById(R.id.textLVL);
            vGold = (TextView) v.findViewById(R.id.textGold);
            vStartDate = (TextView) v.findViewById(R.id.textStartDate);
            vDuration = (TextView) v.findViewById(R.id.textDuration);
            vImageChamp = (NetworkImageView) v.findViewById(R.id.imgChamp);
            isRanked = (ImageView) v.findViewById(R.id.isRanked);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.RelativeLayoutText);
            relativeImage = (RelativeLayout) v.findViewById(R.id.RelativeLayoutImage);
        }
    }





}
