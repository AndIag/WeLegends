package es.coru.andiag.andiag_mvp;

import android.view.View;

/**
 * Created by Canalejas on 11/12/2016.
 */

public class BaseLoadingActivity extends BaseActivity implements BaseLoadingView {
    private final static String TAG = BaseLoadingActivity.class.getSimpleName();

    protected View loadingView;
    private boolean startLoading;

    protected void setProgressBar(int resId) {
        setProgressBar(resId, false);
    }

    protected void setProgressBar(int resId, boolean startLoading) {
        this.loadingView = findViewById(resId);
        this.startLoading = startLoading;
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    }

    @Override
    public void errorLoading(int stringResource) {

    }
}
