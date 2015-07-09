package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andiag.coru.es.welegends.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlayerInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlayerInfo extends Fragment {

    public static FragmentPlayerInfo newInstance() {
        FragmentPlayerInfo fragment = new FragmentPlayerInfo();
        return fragment;
    }

    public FragmentPlayerInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_info, container, false);
    }


}
