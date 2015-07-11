package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.Match;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerMatchDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerMatchDetails extends SwipeRefreshLayoutFragment {

    private static FragmentPlayerMatchDetails fragmentPlayerMatchDetails;

    private Match match;

    public FragmentPlayerMatchDetails() {
        // Required empty public constructor
    }

    public static FragmentPlayerMatchDetails newInstance() {
        if (fragmentPlayerMatchDetails != null) {
            return fragmentPlayerMatchDetails;
        }
        fragmentPlayerMatchDetails = new FragmentPlayerMatchDetails();
        return fragmentPlayerMatchDetails;
    }

    public void setMatch(Match match) {
        this.match = match;
        changeRefreshingValue(false);
    }

    @Override
    protected void initializeRefresh(View view) {
        setRefreshLayout((SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh));
        setColors(null); //NULL means default colors

        changeRefreshingValue(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_info, container, false);

        initializeRefresh(view);

        return view;
    }

}
