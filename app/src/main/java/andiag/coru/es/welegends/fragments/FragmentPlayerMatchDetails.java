package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import andiag.coru.es.welegends.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerMatchDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerMatchDetails extends SwipeRefreshLayoutFragment {

    public FragmentPlayerMatchDetails() {
        // Required empty public constructor
    }

    public static FragmentPlayerMatchDetails newInstance() {
        FragmentPlayerMatchDetails fragmentPlayerMatchDetails = new FragmentPlayerMatchDetails();
        Bundle args = new Bundle();
        fragmentPlayerMatchDetails.setArguments(args);
        return fragmentPlayerMatchDetails;
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
