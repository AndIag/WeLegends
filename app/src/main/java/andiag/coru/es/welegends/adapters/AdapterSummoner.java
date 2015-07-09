package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import andiag.coru.es.welegends.DTOs.SummonerHistory;
import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;
import andiag.coru.es.welegends.utils.static_data.APIHandler;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Andy on 07/07/2015.
 */
public class AdapterSummoner extends BaseAdapter {

    private ArrayList<SummonerHistory> summoners = new ArrayList<>();
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private APIHandler apiHandler;


    public AdapterSummoner(Context context) {
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleyHelper.getInstance(context).getImageLoader();
        apiHandler = APIHandler.getInstance();
    }

    public void updateSummoners(ArrayList<SummonerHistory> summoners){
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
        SummonerHistory summoner = summoners.get(i);
        if(view == null){
            view = inflater.inflate(R.layout.item_summoner,null);
        }

        CircleImageView img = (CircleImageView) view.findViewById(R.id.imageSumm);
        img.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.aatrox));

        NetworkImageView networkImg = (NetworkImageView) view.findViewById(R.id.networkSummImage);
        networkImg.setErrorImageResId(R.drawable.item_default);
        networkImg.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"+apiHandler.getServer_version()+"/img/profileicon/"+
                summoner.getSummoner().getProfileIconId()+".png"
                ,imageLoader);


        TextView text = (TextView) view.findViewById(R.id.textSumm);

        text.setText(summoner.getSummoner().getName());

        return view;

    }
}
