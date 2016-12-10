package es.coru.andiag.welegends.common.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import es.coru.andiag.welegends.common.mvp.base.BaseFragmentPresenter;
import es.coru.andiag.welegends.common.mvp.base.BaseFragmentView;
import es.coru.andiag.welegends.common.mvp.base.BaseView;

/**
 * Created by iagoc on 11/12/2016.
 */

public abstract class BaseFragment<PV extends Context & BaseView> extends Fragment implements BaseFragmentView {
    private final static String TAG = BaseFragment.class.getSimpleName();

    private PV mParentContext;
    private BaseFragmentPresenter<BaseFragment, PV> mPresenter;

    protected void setPresenter(BaseFragmentPresenter<? extends BaseFragment, PV> presenter) {
        mPresenter = (BaseFragmentPresenter<BaseFragment, PV>) presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mParentContext = (PV) context;

        if (mPresenter == null) Log.e(TAG, "Null Presenter: Initialize it on setPresenter method");
        this.mPresenter.attach(this, this.mParentContext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mPresenter.detach();
        this.mPresenter = null;
        this.mParentContext = null;
    }

}
