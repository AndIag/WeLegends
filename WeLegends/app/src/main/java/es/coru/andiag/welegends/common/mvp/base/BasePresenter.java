package es.coru.andiag.welegends.common.mvp.base;

import android.content.Context;

/**
 * Created by iagoc on 10/12/2016.
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
