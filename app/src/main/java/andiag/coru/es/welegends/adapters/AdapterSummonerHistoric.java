package andiag.coru.es.welegends.adapters;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.persistence.Version;
import andiag.coru.es.welegends.rest.RestClient;
import andiag.coru.es.welegends.rest.entities.Summoner;

/**
 * Created by andyq on 15/06/2016.
 */
public class AdapterSummonerHistoric extends BaseQuickAdapter<Summoner> {

    private String version;

    public AdapterSummonerHistoric(int layoutResId, List<Summoner> data) {
        super(layoutResId, data);
        version = Version.getVersion((Activity) mContext);
    }

    @Override
    protected void convert(BaseViewHolder holder, Summoner summoner) {
        holder.setText(R.id.textSummonerName, summoner.getName())
                .setText(R.id.textSummonerRegion, summoner.getRegion());

        Glide.with(mContext).load(RestClient.getProfileIconEndpoint(version)+summoner.getProfileIconId()+".png").asBitmap()
                .placeholder(R.drawable.default_champion)
                .error(R.drawable.default_champion_error)
                .into((RoundedImageView) holder.getView(R.id.imageSummoner));
    }
}
