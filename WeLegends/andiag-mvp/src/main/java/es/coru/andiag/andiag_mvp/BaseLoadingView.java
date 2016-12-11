package es.coru.andiag.andiag_mvp;

/**
 * Created by Canalejas on 11/12/2016.
 */

public interface BaseLoadingView {
    void showLoading();

    void hideLoading();

    void errorLoading(String message);
}
