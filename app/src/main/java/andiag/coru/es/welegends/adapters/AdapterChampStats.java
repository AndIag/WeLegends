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
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityChampionStatsDetails;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

        /*      BUNDLE DATA
        * summonerName      String *solo en la header
        * summonerProfileId int *solo en la header
        * champId           int
        * key               String
        * name              String
        * victories         float
        * defeats           float
        * totalGames        float
        * kills             float
        * death             float
        * assist            float
        * cs                float
        * gold              float
        * penta             int
        * quadra            int
        * triple            int
        * double            int
        * turrets           int
        * dealt             int
        * taken             int
        * */

public class AdapterChampStats extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private List<Bundle> champList = new ArrayList<>();
    private ImageLoader imageLoader;

    public AdapterChampStats(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    // OUTSIDE METHODS

    public boolean needReload(){
        return champList.size()==0;
    }

    public void updateChamps(List<Bundle> cL) {
        if (cL != null) {
            this.champList = cL;
            notifyDataSetChanged();
        }
    }

    public void clearChamps() {
        champList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_champions_stats, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_champions_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bundle item = getItem(position);
        float totalGames, kills, death, assist;
        totalGames = item.getFloat("totalGames");
        kills = item.getFloat("kills");
        death = item.getFloat("death");
        assist = item.getFloat("assist");

        if (holder instanceof VHItem) {
            VHItem h = (VHItem) holder;

            h.textD.setText(String.valueOf((int) item.getFloat("defeats")));
            h.textV.setText(String.valueOf((int) item.getFloat("victories")));

            h.textKDA.setText(String.format("%.1f", kills / totalGames)
                    + "/" + String.format("%.1f", death / totalGames)
                    + "/" + String.format("%.1f", assist / totalGames));

            h.textCS.setText(String.format("%.1f", item.getFloat("cs")/totalGames));
            h.textGold.setText(String.format("%.1f", item.getFloat("gold") / (totalGames * 1000)) + "k");

            h.imageChamp.setErrorImageResId(R.drawable.default_champion);
            h.imageChamp.setDefaultImageResId(R.drawable.default_champion);
            h.imageChamp.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                            + ChampionsHandler.getServerVersion((Activity) context)
                            + "/img/champion/" + item.getString("key") + ".png",
                    imageLoader);
        } else if (holder instanceof VHHeader) {
            final VHHeader h = (VHHeader) holder;
            h.textVictories.setText(String.valueOf((int) item.getFloat("victories")));
            h.textDefeats.setText(String.valueOf((int) item.getFloat("defeats")));

            h.textGlobalKDA.setText(String.format("%.1f", kills / totalGames)
                    + "/" + String.format("%.1f", death / totalGames)
                    + "/" + String.format("%.1f", assist / totalGames));

            h.textPercent.setText(String.format("%.2f", (item.getFloat("victories")/totalGames) * 100) + "%");

            String championImg = item.getString("key")+"_1.jpg";

            h.background.setErrorImageResId(R.drawable.gnar_0);
            h.background.setImageUrl("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championImg,imageLoader);
        }
    }

    @Override
    public int getItemCount() {
        return champList.size();
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
        return champList.get(position);
    }

    class VHItem extends RecyclerView.ViewHolder {
        protected CardView cardView;
        TextView textKDA,textGold,textCS,textV,textD;
        NetworkImageView imageChamp;
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;
            cardView = (CardView) v.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ActivityChampionStatsDetails.class);
                    i.putExtra("champData", getItem(getAdapterPosition()));
                    i.putExtra("isHeader", false);
                    context.startActivity(i);
                }
            });
            textCS = (TextView) v.findViewById(R.id.textCS);
            textKDA = (TextView) v.findViewById(R.id.textKDA);
            textGold = (TextView) v.findViewById(R.id.textGold);
            textV = (TextView) v.findViewById(R.id.textV);
            textD = (TextView) v.findViewById(R.id.textD);
            imageChamp = (NetworkImageView) v.findViewById(R.id.imgChamp);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        protected CardView cardView;
        TextView textVictories,textDefeats,textGlobalKDA,textPercent;
        NetworkImageView background;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view=itemView;
            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ActivityChampionStatsDetails.class);
                    i.putExtra("champData", getItem(0));
                    i.putExtra("isHeader", true);
                    context.startActivity(i);
                }
            });
            background = (NetworkImageView) view.findViewById(R.id.imageBackground);
            textVictories = (TextView) view.findViewById(R.id.textVictories);
            textDefeats = (TextView) view.findViewById(R.id.textDefeats);
            textGlobalKDA = (TextView) view.findViewById(R.id.textGlobalKDA);
            textPercent = (TextView) view.findViewById(R.id.textPercent);

        }
    }
}
