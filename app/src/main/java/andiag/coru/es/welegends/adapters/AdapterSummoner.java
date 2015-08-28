package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivitySummoner;
import andiag.coru.es.welegends.entities.DTOs.SummonerHistoryDto;
import andiag.coru.es.welegends.utils.CircledNetworkImageView;
import andiag.coru.es.welegends.utils.handlers.API;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/**
 * Created by Andy on 07/07/2015.
 */
public class AdapterSummoner extends BaseAdapter {

    private ArrayList<SummonerHistoryDto> summoners = new ArrayList<>();
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public AdapterSummoner(ActivitySummoner context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
    }

    public void updateSummoners(ArrayList<SummonerHistoryDto> summoners) {
        this.summoners = summoners;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return summoners.size();
    }

    @Override
    public Object getItem(int i) {
        return summoners.get(i);
    }

    @Override
    public long getItemId(int i) {
        return summoners.get(i).getSummoner().getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SummonerHistoryDto summoner = summoners.get(i);
        if (view == null) {
            view = inflater.inflate(R.layout.item_summoner_history, null);
        }

        CircledNetworkImageView networkImg = (CircledNetworkImageView) view.findViewById(R.id.networkSummImage);
        networkImg.setErrorImageResId(R.drawable.default_champion_error);
        networkImg.setDefaultImageResId(R.drawable.default_champion);
        networkImg.setImageUrl(API.getProfileIcon(summoner.getSummoner().getProfileIconId()), imageLoader);

        TextView text = (TextView) view.findViewById(R.id.textSumm);

        text.setText(summoner.getSummoner().getName());

        return view;

    }
}
