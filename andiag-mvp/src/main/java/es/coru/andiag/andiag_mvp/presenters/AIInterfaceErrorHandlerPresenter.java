package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;

/**
 * Created by Canalejas on 16/12/2016.
 */

public interface AIInterfaceErrorHandlerPresenter<T> {

    Context getContext();

    void onLoadError(String message);

    void onLoadError(int resId);

}
