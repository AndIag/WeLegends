package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;

/**
 * Created by Iago on 11/07/2015.
 */
public class FragmentVictoryDefeatDetails extends SwipeRefreshLayoutFragment {

    private static ActivityDetails activityMain;

    public FragmentVictoryDefeatDetails() {
    }

    public static FragmentVictoryDefeatDetails newInstance() {
        return new FragmentVictoryDefeatDetails();
    }

    //SAVE AND RETRIEVE DATA
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRetrieveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) { //Load saved data in onPause
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityMain = (ActivityDetails) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRetrieveInstanceState(savedInstanceState);
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
        changeRefreshingValue(true);
    }

}
