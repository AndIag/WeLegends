package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;

import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView;

/**
 * Created by Canalejas on 10/12/2016.
 */

public abstract class AIActivityPresenter<V extends Context & AIInterfaceActivityView> implements AIInterfaceActivityPresenter<V> {
    private final static String TAG = AIActivityPresenter.class.getSimpleName();

    private V mView;

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public final void attach(V view) {
        this.mView = view;
        onViewAttached();
    }

    @Override
    public final void detach() {
        this.mView = null;
        onViewDetached();
    }

    public abstract void onViewAttached();

    public abstract void onViewDetached();

}
