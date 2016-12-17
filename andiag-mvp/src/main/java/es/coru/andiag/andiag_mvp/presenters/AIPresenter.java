package es.coru.andiag.andiag_mvp.presenters;

import android.support.annotation.NonNull;

/**
 * Created by Iago on 17/12/2016.
 */

public abstract class AIPresenter<C, V> implements AIInterfacePresenter<C, V> {

    private V mView;
    private C mContext;
    private boolean isViewCreated = false;

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public C getContext() {
        return mContext;
    }

    @Override
    public final void attach(C context, @NonNull V view) {
        this.mView = view;
        this.mContext = context;
        onViewAttached();
    }

    @Override
    public final void detach() {
        this.mView = null;
        this.mContext = null;
        onViewDetached();
    }

    @Override
    public boolean isViewAttached() {
        return isViewCreated;
    }

    @Override
    public boolean hasContext() {
        return mContext != null;
    }

    public abstract void onViewAttached();

    public void onViewDetached() {
        isViewCreated = false;
    }

    public void onViewCreated() {
        isViewCreated = true;
    }
}
