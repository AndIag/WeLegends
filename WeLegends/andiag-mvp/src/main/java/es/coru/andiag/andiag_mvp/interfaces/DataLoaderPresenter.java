package es.coru.andiag.andiag_mvp.interfaces;

import android.content.Context;

/**
 * Created by Canalejas on 14/12/2016.
 */

public interface DataLoaderPresenter<T> {
    Context getContext();

    void onLoadSuccess(String message, T data);

    void onLoadProgressChange(String message, T data);

    void onLoadError(String message);
}
