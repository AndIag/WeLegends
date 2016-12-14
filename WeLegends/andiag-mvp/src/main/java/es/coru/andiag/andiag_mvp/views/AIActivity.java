package es.coru.andiag.andiag_mvp.views;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import es.coru.andiag.andiag_mvp.presenters.AIActivityPresenter;


/**
 * Created by Canalejas on 11/12/2016.
 */

public abstract class AIActivity<P extends AIActivityPresenter> extends AppCompatActivity implements AIInterfaceActivityView {
    private final static String TAG = AIActivity.class.getSimpleName();

    private P mPresenter;

    protected void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter == null) Log.e(TAG, "Null Presenter: Initialize it on setPresenter method");
        this.mPresenter.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mPresenter.detach();
        this.mPresenter = null;
    }

}
