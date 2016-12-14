package es.coru.andiag.andiag_mvp.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import es.coru.andiag.andiag_mvp.interfaces.BaseFragmentView;
import es.coru.andiag.andiag_mvp.interfaces.BaseView;

/**
 * Created by Canalejas on 11/12/2016.
 */

public abstract class BaseFragmentPresenter<V extends Fragment & BaseFragmentView, PV extends Context & BaseView> {

    private final static String TAG = BaseFragmentPresenter.class.getSimpleName();

    private V mView;
    private PV mParent;

    public V getView() {
        return mView;
    }

    public PV getParent() {
        return mParent;
    }

    public final void attach(V view, PV parentView) {
        this.mView = view;
        this.mParent = parentView;
        onViewAttached();
    }

    public final void detach() {
        this.mView = null;
        this.mParent = null;
        onViewDetached();
    }

    protected abstract void onViewAttached();

    protected abstract void onViewDetached();

}
