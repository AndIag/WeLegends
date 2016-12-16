package es.coru.andiag.andiag_mvp.presenters;

/**
 * Created by Canalejas on 14/12/2016.
 */

public interface AIInterfaceLoaderPresenter<T> extends AIInterfaceErrorHandlerPresenter<T> {

    void onLoadSuccess(T data);

    void onLoadProgressChange(String message);

    void onLoadProgressChange(int resId);

}
