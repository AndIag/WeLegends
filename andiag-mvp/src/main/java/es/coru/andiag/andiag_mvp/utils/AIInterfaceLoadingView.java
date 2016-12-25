package es.coru.andiag.andiag_mvp.utils;

/**
 * Created by Canalejas on 11/12/2016.
 */

public interface AIInterfaceLoadingView {

    boolean isLoading();

    void showLoading();

    void hideLoading();

    void errorLoading(String message);

    void errorLoading(int resId);
}
