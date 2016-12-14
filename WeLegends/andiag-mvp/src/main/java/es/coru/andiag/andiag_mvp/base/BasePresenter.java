package es.coru.andiag.andiag_mvp.base;

import android.content.Context;

import es.coru.andiag.andiag_mvp.interfaces.BaseView;

/**
 * Created by Canalejas on 10/12/2016.
 */

public abstract class BasePresenter<V extends Context & BaseView> {
    private final static String TAG = BasePresenter.class.getSimpleName();

    private V mView;

    public V getView() {
        return mView;
    }

    public final void attach(V view) {
        this.mView = view;
        onViewAttached();
    }

    public final void detach() {
        this.mView = null;
        onViewDetached();
    }

    public abstract void onViewAttached();

    public abstract void onViewDetached();

}
