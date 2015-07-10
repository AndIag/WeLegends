package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.entities.Match;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerInfo extends Fragment {

    private static FragmentPlayerInfo fragmentPlayerInfo;
    private Match match;

    public FragmentPlayerInfo() {
        // Required empty public constructor
    }

    public static FragmentPlayerInfo newInstance() {
        if (fragmentPlayerInfo != null) {
            return fragmentPlayerInfo;
        }
        fragmentPlayerInfo = new FragmentPlayerInfo();
        return fragmentPlayerInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_info, container, false);
    }

}
