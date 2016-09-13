package andiag.coru.es.welegends.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import andiag.coru.es.welegends.activities.ActivitySummonerData;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Canalejas on 12/09/2016.
 */
public abstract class FragmentBase extends Fragment {

    protected Activity parentActivity;

    Unbinder bind;

    public void setParentActivityTitle(String title){
        parentActivity.setTitle(title);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.parentActivity = (Activity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    protected abstract int getFragmentLayout();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    private void injectViews(final View view) {
        bind = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
