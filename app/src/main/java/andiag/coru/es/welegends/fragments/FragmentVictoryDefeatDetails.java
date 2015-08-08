package andiag.coru.es.welegends.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import andiag.coru.es.welegends.R;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentVictoryDefeatDetails extends SwipeRefreshLayoutFragment {

    public FragmentVictoryDefeatDetails() {
    }

    public static FragmentVictoryDefeatDetails newInstance() {
        FragmentVictoryDefeatDetails fragmentVictoryDefeatDetails = new FragmentVictoryDefeatDetails();
        Bundle args = new Bundle();
        fragmentVictoryDefeatDetails.setArguments(args);
        return fragmentVictoryDefeatDetails;
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
        changeRefreshingValue(true);
    }

}
