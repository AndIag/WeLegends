package es.coru.andiag.andiag_mvp.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import es.coru.andiag.andiag_mvp.views.AIFragment;
import es.coru.andiag.andiag_mvp.views.AIInterfaceLoadingView;

/**
 * Created by Canalejas on 11/12/2016.
 */

public class AIFragmentLoading extends AIFragment implements AIInterfaceLoadingView {
    private final static String TAG = AIFragmentLoading.class.getSimpleName();

    protected View loadingView;
    private boolean startLoading;

    protected void setProgressBar(int resId) {
        setProgressBar(resId, false);
    }

    protected void setProgressBar(int resId, boolean startLoading) {
        this.loadingView = getActivity().findViewById(resId);
        this.startLoading = startLoading;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (startLoading && loadingView != null) {
            showLoading();
        }
    }

    @Override
    public boolean isLoading() {
        return loadingView != null && loadingView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void errorLoading(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorLoading(int stringResource) {
        Toast.makeText(getContext(), this.getString(stringResource), Toast.LENGTH_SHORT).show();
    }
}
