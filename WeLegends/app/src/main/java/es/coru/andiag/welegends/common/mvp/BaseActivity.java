package es.coru.andiag.welegends.common.mvp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import es.coru.andiag.welegends.common.mvp.base.BasePresenter;
import es.coru.andiag.welegends.common.mvp.base.BaseView;

/**
 * Created by iagoc on 11/12/2016.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private final static String TAG = BaseActivity.class.getSimpleName();

    private BasePresenter<BaseActivity> mPresenter;

    protected void setPresenter(BasePresenter<? extends BaseActivity> presenter) {
        mPresenter = (BasePresenter<BaseActivity>) presenter;
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
