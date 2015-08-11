package andiag.coru.es.welegends.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;
import andiag.coru.es.welegends.utils.champions.ChampionsHandler;
import andiag.coru.es.welegends.utils.requests.VolleyHelper;

/*
            Int     "championId"
            Int     "spell1"
            Int     "spell2"
            Long    "kills"
            Long    "deaths"
            Long    "assists"
            Long    "cs"
            Long    "gold"
            Long    "item0"
            Long    "item1"
            Long    "item2"
            Long    "item3"
            Long    "item4"
            Long    "item5"
            Long    "item6"
*/
public class FragmentPlayerMatchDetails extends SwipeRefreshLayoutFragment {

    private static ActivityDetails activityMain;
    private TextView textK, textD, textA, textKDA, textCS, textGold;
    private NetworkImageView imgTotem, imgIt1, imgIt2, imgIt3, imgIt4, imgIt5, imgIt6, imageChampion;
    private Bundle data;
    private ImageLoader imageLoader;

    public FragmentPlayerMatchDetails() {
        // Required empty public constructor
    }

    public static FragmentPlayerMatchDetails newInstance() {
        return new FragmentPlayerMatchDetails();
    }

    public void setData(Bundle data) {
        this.data = data;
        setDataOnView();
    }

    private void setDataOnView() {
        textK.setText(String.valueOf(data.getLong("kills")));
        textD.setText(String.valueOf(data.getLong("deaths")));
        textA.setText(String.valueOf(data.getLong("assists")));
        long deaths = data.getLong("deaths");
        if (deaths == 0) deaths = 1;
        float KDA = (data.getLong("kills") + data.getLong("assists")) / deaths;
        textKDA.setText(String.format("%.1f", KDA));

        textCS.setText(String.valueOf(data.getLong("cs")));
        textGold.setText(String.valueOf(data.getLong("gold") / 1000 + "k"));

        imageChampion.setErrorImageResId(R.drawable.item_default);
        imageChampion.setDefaultImageResId(R.drawable.item_default);
        imageChampion.setImageUrl("http://ddragon.leagueoflegends.com/cdn/"
                        + ChampionsHandler.getServerVersion(getActivity())
                        + "/img/champion/" + ChampionsHandler.getChampKey(data.getInt("championId")) + ".png",
                imageLoader);

        setItemImage(imgTotem, data.getLong("item6"));
        setItemImage(imgIt1, data.getLong("item0"));
        setItemImage(imgIt2, data.getLong("item1"));
        setItemImage(imgIt3, data.getLong("item2"));
        setItemImage(imgIt4, data.getLong("item3"));
        setItemImage(imgIt5, data.getLong("item4"));
        setItemImage(imgIt6, data.getLong("item5"));
    }

    private void setItemImage(NetworkImageView imgView, long id) {
        if (id > 0) {
            imgView.setErrorImageResId(R.drawable.item_default);
            imgView.setDefaultImageResId(R.drawable.item_default);
            imgView.setImageUrl("http://ddragon.leagueoflegends.com/cdn/" +
                            ChampionsHandler.getServerVersion(getActivity()) + "/img/item/" + id + ".png",
                    imageLoader);
        } else {
            imgView.setImageResource(R.drawable.item_default);
        }
    }

    //SAVE AND RETRIEVE DATA
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(data);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) { //Load saved data in onPause
            data = savedInstanceState;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityDetails) activity;
        activityMain.setFragmentPlayerMatchDetails(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);
        imageLoader = VolleyHelper.getInstance(getActivity()).getImageLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors
        setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changeRefreshingValue(false);
            }
        });
        //changeRefreshingValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, container, false);

        initializeRefresh(view);

        textK = (TextView) view.findViewById(R.id.textK);
        textD = (TextView) view.findViewById(R.id.textD);
        textA = (TextView) view.findViewById(R.id.textA);
        textKDA = (TextView) view.findViewById(R.id.textKDA);
        textCS = (TextView) view.findViewById(R.id.textCS);
        textGold = (TextView) view.findViewById(R.id.textGold);
        imageChampion = (NetworkImageView) view.findViewById(R.id.imageChampion);
        imgTotem = (NetworkImageView) view.findViewById(R.id.imgTotem);
        imgIt1 = (NetworkImageView) view.findViewById(R.id.imgIt1);
        imgIt2 = (NetworkImageView) view.findViewById(R.id.imgIt2);
        imgIt3 = (NetworkImageView) view.findViewById(R.id.imgIt3);
        imgIt4 = (NetworkImageView) view.findViewById(R.id.imgIt4);
        imgIt5 = (NetworkImageView) view.findViewById(R.id.imgIt5);
        imgIt6 = (NetworkImageView) view.findViewById(R.id.imgIt6);

        return view;
    }

}
