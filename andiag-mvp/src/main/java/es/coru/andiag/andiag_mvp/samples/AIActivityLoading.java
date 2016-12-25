package es.coru.andiag.andiag_mvp.samples;

import android.view.View;
import android.widget.Toast;

import es.coru.andiag.andiag_mvp.utils.AIInterfaceLoadingView;
import es.coru.andiag.andiag_mvp.views.AIActivity;

/**
 * Created by Canalejas on 11/12/2016.
 */

public class AIActivityLoading extends AIActivity implements AIInterfaceLoadingView {
    private final static String TAG = AIActivityLoading.class.getSimpleName();

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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void errorLoading(int resId) {
        Toast.makeText(this, this.getString(resId), Toast.LENGTH_SHORT).show();
    }
}
