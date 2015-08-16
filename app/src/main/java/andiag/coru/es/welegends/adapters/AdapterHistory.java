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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

        /*      BUNDLE DATA
        * matchId           long
        * champId           int
        * champName         String
        * champKey          String
        * mapId             int
        * mapName           int(Resource)
        * mapImage          int(Resource)
        * kills             long
        * death             long
        * assist            long
        * lvl               long
        * cs                long
        * gold              float
        * winner            boolean
        * startDate         String
        * duration          String
        * matchType         int(Resource)
        * */

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
                inflate(R.layout.item_match_history, viewGroup, false);

        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int i) {
        Bundle bundle = historyList.get(i);

        holder.position = i;
        holder.vCS.setText(String.valueOf(bundle.getLong("cs")));
        holder.vGold.setText(String.format("%.1f", bundle.getFloat("gold") / 1000) + "k");
        holder.vLVL.setText(String.valueOf(bundle.getLong("lvl")));
        holder.vKDA.setText(bundle.getLong("kills") + "/"
                + bundle.getLong("death") + "/" + bundle.getLong("assist"));
        holder.vStartDate.setText(bundle.getString("startDate"));
        holder.vDuration.setText(bundle.getString("duration"));
        holder.vMap.setText(bundle.getInt("mapName"));
        holder.relativeImage.setBackgroundResource(bundle.getInt("mapImage"));
        holder.vChampName.setText(bundle.getString("champName"));
        holder.vImageChamp.setErrorImageResId(R.drawable.default_champion_error);
        holder.vImageChamp.setDefaultImageResId(R.drawable.default_champion);
        holder.vImageChamp.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                        + ChampionsHandler.getServerVersion((Activity) context)
                        + "/img/champion/" + bundle.getString("champKey") + ".png",
                imageLoader);

        holder.isRanked.setImageResource(bundle.getInt("matchType"));
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
                    i.putExtra("principalChamp", historyList.get(position).getInt("champId"));
                    i.putExtra("isWinner", historyList.get(position).getBoolean("winner"));
                    context.startActivity(i);
                }
            });

            vChampName = (TextView) v.findViewById(R.id.textName);
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
