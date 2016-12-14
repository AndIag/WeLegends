package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;
import android.support.v4.app.Fragment;

import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView;
import es.coru.andiag.andiag_mvp.views.AIInterfaceFragmentView;

/**
 * Created by Canalejas on 11/12/2016.
 */

public abstract class AIFragmentPresenter
        <V extends Fragment & AIInterfaceFragmentView, PV extends Context & AIInterfaceActivityView> implements AIInterfaceFragmentPresenter<V, PV> {

    private final static String TAG = AIFragmentPresenter.class.getSimpleName();

    private V mView;
    private PV mParent;

    @Override
    public V getView() {
        return mView;
    }

    @Override
    public PV getParent() {
        return mParent;
    }

    @Override
    public final void attach(V view, PV parentView) {
        this.mView = view;
        this.mParent = parentView;
        onViewAttached();
    }

    @Override
    public final void detach() {
        this.mView = null;
        this.mParent = null;
        onViewDetached();
    }

    protected abstract void onViewAttached();

    protected abstract void onViewDetached();

}
