package es.coru.andiag.welegends.common.mvp.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by iagoc on 11/12/2016.
 */

public abstract class BaseFragmentPresenter<V extends Fragment & BaseFragmentView, PV extends Context & BaseView> {

    private final static String TAG = BaseFragmentPresenter.class.getSimpleName();

    protected V mView;
    protected PV mParent;

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

    public abstract void onViewAttached();

    public abstract void onViewDetached();

}
