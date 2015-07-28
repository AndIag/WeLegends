package andiag.coru.es.welegends.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by andyq on 21/07/2015.
 */
public class AdapterRankedChamps extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private Context context;
    private List<Bundle> champList = new ArrayList<>();
    private ImageLoader imageLoader;

    public AdapterRankedChamps(Context context) {
        this.context = context;
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    // OUTSIDE METHODS

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
        if (holder instanceof VHItem) {
            VHItem h = (VHItem) holder;
            h.textCS.setText(item.getString("cs"));
            h.textGold.setText(item.getString("gold"));
            h.textD.setText(item.getString("defeats"));
            h.textV.setText(item.getString("victories"));
            h.textKDA.setText(item.getString("kda"));
            h.imageChamp.setImageBitmap(ChampionsHandler.getChampImage((Activity) context, item.getInt("imageId")));
            //cast holder to VHItem and set data
        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            final VHHeader h = (VHHeader) holder;
            h.textVictories.setText(item.getString("victories"));
            h.textDefeats.setText(item.getString("defeats"));
            h.textGlobalKDA.setText(item.getString("globalkda"));
            h.textPercent.setText(item.getString("percent"));

            int myWidth = 650;
            int myHeight = 384;
            String championImg = ChampionsHandler.getChampKey(item.getInt("image"))+"_0.jpg";

            imageLoader.get("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + championImg,
                    new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    h.background.setBackground(new BitmapDrawable(context.getResources(), response.getBitmap()));
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //h.background.setBackground();
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
        return position == 0;
    }

    private Bundle getItem(int position) {
        return champList.get(position);
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView textKDA,textGold,textCS,textV,textD;
        ImageView imageChamp;
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;
            textCS = (TextView) v.findViewById(R.id.textCS);
            textKDA = (TextView) v.findViewById(R.id.textKDA);
            textGold = (TextView) v.findViewById(R.id.textGold);
            textV = (TextView) v.findViewById(R.id.textV);
            textD = (TextView) v.findViewById(R.id.textD);
            imageChamp =(ImageView) v.findViewById(R.id.imgChamp);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView textVictories,textDefeats,textGlobalKDA,textPercent;
        RelativeLayout background;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view=itemView;
            background = (RelativeLayout) view.findViewById(R.id.relativeBackground);
            textVictories = (TextView) view.findViewById(R.id.textVictories);
            textDefeats = (TextView) view.findViewById(R.id.textDefeats);
            textGlobalKDA = (TextView) view.findViewById(R.id.textGlobalKDA);
            textPercent = (TextView) view.findViewById(R.id.textPercent);

        }
    }
}
