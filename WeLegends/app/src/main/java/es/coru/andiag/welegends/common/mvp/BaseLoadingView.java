package es.coru.andiag.welegends.common.mvp;

/**
 * Created by iagoc on 11/12/2016.
 */

public interface BaseLoadingView {
    void showLoading();

    void hideLoading();

    void errorLoading(String message);
}
