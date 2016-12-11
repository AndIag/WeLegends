package es.coru.andiag.andiag_mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Canalejas on 11/12/2016.
 */

public class BaseLoadingFragment extends BaseFragment implements BaseLoadingView {
    private final static String TAG = BaseLoadingFragment.class.getSimpleName();

    protected ProgressBar mProgressBar;
    private boolean startLoading;

    protected void setProgressBar(int resId) {
        setProgressBar(resId, false);
    }

    protected void setProgressBar(int resId, boolean startLoading) {
        this.mProgressBar = (ProgressBar) getActivity().findViewById(resId);
        this.startLoading = startLoading;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (startLoading && mProgressBar != null) {
            showLoading();
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void errorLoading(String message) {

    }
}
