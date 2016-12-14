package es.coru.andiag.andiag_mvp.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import es.coru.andiag.andiag_mvp.presenters.AIFragmentPresenter;


/**
 * Created by Canalejas on 11/12/2016.
 */

public abstract class AIFragment<P extends AIFragmentPresenter> extends Fragment implements AIInterfaceFragmentView {
    private final static String TAG = AIFragment.class.getSimpleName();

    protected Context mParentContext;
    private P mPresenter;

    protected void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mParentContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
