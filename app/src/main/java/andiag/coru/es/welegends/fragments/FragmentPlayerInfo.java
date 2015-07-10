package andiag.coru.es.welegends.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
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
    private SwipeRefreshLayout refreshLayout;
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

    public void setMatch(Match match) {
        this.match = match;
        changeRefreshingValue(false);
    }

    public void changeRefreshingValue(boolean bool) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(bool);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_info, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        int[] colors = {R.color.swype_1, R.color.swype_2, R.color.swype_3, R.color.swype_4};
        refreshLayout.setColorSchemeResources(colors);

        refreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        changeRefreshingValue(true);

        return view;
    }

}
