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

import java.util.ArrayList;
import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by iagoc on 03/09/2015.
 */
public class AdapterCurrentGameTeams extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Bundle> teamMembers = new ArrayList<>();
    private ImageLoader imageLoader;

    public AdapterCurrentGameTeams(Context context) {
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
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_current_participant, parent, false);
            return new VHItem(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item_team_header, parent, false);
            return new VHHeader(itemView);
        }

        throw new RuntimeException("There is no isWinner that matches the isWinner " + viewType + " + make sure your using types correctly");
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

        } else if (holder instanceof VHHeader) {
        }

    }

    class VHItem extends RecyclerView.ViewHolder {
        View v;

        public VHItem(View itemView) {
            super(itemView);
            this.v = itemView;
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView textBoolean;
        TextView textBanned1, textBanned2, textBanned3;
        CircledNetworkImageView imageBanned1, imageBanned2, imageBanned3;
        ImageView imageDragon, imageBaron;
        View view;

        public VHHeader(View itemView) {
            super(itemView);
            this.view = itemView;
            textBoolean = (TextView) view.findViewById(R.id.textBoolean);
            imageBaron = (ImageView) view.findViewById(R.id.imageBaron);
            imageDragon = (ImageView) view.findViewById(R.id.imageDragon);
            imageBanned1 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned1);
            imageBanned2 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned2);
            imageBanned3 = (CircledNetworkImageView) view.findViewById(R.id.imageBanned3);
            textBanned1 = (TextView) view.findViewById(R.id.textBanned1);
            textBanned2 = (TextView) view.findViewById(R.id.textBanned2);
            textBanned3 = (TextView) view.findViewById(R.id.textBanned3);


            (view.findViewById(R.id.textKDA)).setVisibility(View.GONE);
            (view.findViewById(R.id.textBaron)).setVisibility(View.GONE);
            (view.findViewById(R.id.textDragon)).setVisibility(View.GONE);
            (view.findViewById(R.id.imageBaron)).setVisibility(View.GONE);
            (view.findViewById(R.id.imageDragon)).setVisibility(View.GONE);
        }
    }

}
