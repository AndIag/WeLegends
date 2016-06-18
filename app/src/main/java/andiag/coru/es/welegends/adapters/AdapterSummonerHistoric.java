package andiag.coru.es.welegends.adapters;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.rest.entities.Summoner;

/**
 * Created by andyq on 15/06/2016.
 */
public class AdapterSummonerHistoric extends BaseQuickAdapter<Summoner> {

    public AdapterSummonerHistoric(Context context, int layoutResId, List<Summoner> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Summoner summoner) {
        holder.setText(R.id.textSummonerName, summoner.getName())
                .setText(R.id.textSummonerRegion, summoner.getRegion());

    }
}
