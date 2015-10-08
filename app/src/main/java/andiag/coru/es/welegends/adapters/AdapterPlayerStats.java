package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by andyq on 28/07/2015.
 */
public class AdapterPlayerStats extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_DIVIDER = 2;
    public static final int TYPE_STATUS = 3;

    private Context context;
    private ImageLoader imageLoader;
    // Data
    private List<Bundle> stats = new ArrayList<>();
    private boolean playing = false;

    public AdapterPlayerStats(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    // OUTSIDE METHODS

    public void updateStats(List<Bundle> cL) {
        if (cL != null) {
            this.stats = cL;
            notifyDataSetChanged();
        }
    }

    public void clearStats() {
        stats.clear();
        notifyDataSetChanged();
    }

    public void updatePlayerStatus(boolean b) {
        this.playing = b;
        if (stats != null) notifyItemChanged(1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_summoner_stats_item, parent, false);
            return new VHItem(itemView);

        } else if (viewType == TYPE_HEADER) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_summoner_stats_header, parent, false);
            return new VHHeader(itemView);

        } else if (viewType == TYPE_DIVIDER) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_summoner_stats_divider, parent, false);
            return new VHDivider(itemView);

        } else if (viewType == TYPE_STATUS) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_summoner_stats_status, parent, false);
            return new VHDivider(itemView);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bundle item = getItem(position);

        if (holder instanceof VHHeader) {
            VHHeader h = (VHHeader) holder;
            h.textLevel.setText(item.getString("level"));
            h.textSumName.setText(item.getString("summoner"));
            h.textServer.setText(item.getString("server"));
            h.playerIcon.setErrorImageResId(R.drawable.default_champion_error);
            h.playerIcon.setDefaultImageResId(R.drawable.default_champion);
            h.playerIcon.setImageUrl(item.getString("url"), imageLoader);

        } else if (holder instanceof VHDivider) {
            VHDivider h = (VHDivider) holder;
            h.divider.setText(item.getString("divider"));
            //if (getItemViewType(position)==TYPE_STATUS) h.v.setBackgroundColor(item.getInt("color"));
            if (getItemViewType(position)==TYPE_STATUS){
                if (playing) {
                    h.v.setClickable(true);
                    h.divider.setText(context.getString(R.string.playing_true));
                    h.v.setCardBackgroundColor(context.getResources().getColor(R.color.win));
                } else {
                    h.v.setClickable(false);
                    h.divider.setText(context.getString(R.string.playing_false));
                    h.v.setCardBackgroundColor(context.getResources().getColor(R.color.lose));
                }
            }



        } else if (holder instanceof VHItem) {
            VHItem h = (VHItem) holder;
            h.textDivision.setText(item.getString("division"));
            h.textDivName.setText(item.getString("divname"));
            h.textLosses.setText(item.getString("losses"));
            h.textWins.setText(item.getString("wins"));
            h.textLP.setText(item.getString("lp") + " LP");
            h.textTeamName.setText(item.getString("name"));
            h.imageRanked.setImageResource(item.getInt("image"));

        }
    }

    @Override
    public int getItemCount(){
        return stats.size();
    }

    private Bundle getItem(int position) {
        return stats.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        Bundle x = stats.get(position);
        return x.getInt("type");
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView textTeamName, textDivision, textDivName, textLP, textWins, textLosses;
        ImageView imageRanked;
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;
            textTeamName = (TextView) v.findViewById(R.id.textTeamName);
            textDivision = (TextView) v.findViewById(R.id.textDivision);
            textDivName = (TextView) v.findViewById(R.id.textDivName);
            textLP = (TextView) v.findViewById(R.id.textLP);
            textWins = (TextView) v.findViewById(R.id.textWins);
            textLosses = (TextView) v.findViewById(R.id.textLosses);
            imageRanked = (ImageView) v.findViewById(R.id.imageRanked);
        }
    }

    class VHDivider extends RecyclerView.ViewHolder {
        CardView v;
        TextView divider;

        public VHDivider(View itemView) {
            super(itemView);
            this.v = (CardView) itemView;
            divider = (TextView) v.findViewById(R.id.textGroup);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView textSumName, textServer, textLevel, textPlayerStatus;
        CircledNetworkImageView playerIcon;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view = itemView;
            playerIcon = (CircledNetworkImageView) view.findViewById(R.id.imageSummoner);
            textSumName = (TextView) view.findViewById(R.id.textName);
            textServer = (TextView) view.findViewById(R.id.textServer);
            textLevel = (TextView) view.findViewById(R.id.textLevel);
            textPlayerStatus = (TextView) view.findViewById(R.id.textPlaying);
        }
    }
}
