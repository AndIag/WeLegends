package andiag.coru.es.welegends.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerMatchDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerMatchDetails extends SwipeRefreshLayoutFragment {

    private static ActivityDetails activityMain;

    public FragmentPlayerMatchDetails() {
        // Required empty public constructor
    }

    public static FragmentPlayerMatchDetails newInstance() {
        return new FragmentPlayerMatchDetails();
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
        //changeRefreshingValue(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, container, false);

        initializeRefresh(view);

        return view;
    }

}
