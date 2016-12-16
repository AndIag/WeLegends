package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;

/**
 * Created by Canalejas on 14/12/2016.
 */

public interface AIInterfaceLoaderPresenter<T> {
    Context getContext();

    void onLoadSuccess(T data);

    void onLoadProgressChange(String message);

    void onLoadError(String message);

    void onLoadError(int resId);
}
