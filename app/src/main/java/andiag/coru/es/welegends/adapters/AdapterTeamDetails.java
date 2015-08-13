package andiag.coru.es.welegends.adapters;

import android.content.Context;
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
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by andyq on 13/08/2015.
 */
public class AdapterTeamDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Bundle> teamMembers = new ArrayList<>();
    private ImageLoader imageLoader;

    public AdapterTeamDetails(Context context) {
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
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_participant, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
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

        } else if (holder instanceof VHHeader) {
            final VHHeader h = (VHHeader) holder;

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
        TextView textBoolean,textBanned1,textBanned2,textBanned3,textKDA,textBaron,textDragon;
        NetworkImageView imageBanned1,imageBanned2,imageBanned3;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view=itemView;
            imageBanned1 = (NetworkImageView) view.findViewById(R.id.imageBanned1);
            imageBanned2 = (NetworkImageView) view.findViewById(R.id.imageBanned2);
            imageBanned3 = (NetworkImageView) view.findViewById(R.id.imageBanned3);
            textBoolean = (TextView) view.findViewById(R.id.textBoolean);
            textBanned1 = (TextView) view.findViewById(R.id.textBanned1);
            textBanned2 = (TextView) view.findViewById(R.id.textBanned2);
            textBanned3 = (TextView) view.findViewById(R.id.textBanned3);

            textKDA = (TextView) view.findViewById(R.id.textKDA);
            textBaron = (TextView) view.findViewById(R.id.textBaron);
            textDragon = (TextView) view.findViewById(R.id.textDragon);

        }
    }

}
