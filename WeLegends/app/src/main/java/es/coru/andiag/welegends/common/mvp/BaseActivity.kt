package es.coru.andiag.welegends.common.mvp

import android.support.v7.app.AppCompatActivity
import android.util.Log
import es.coru.andiag.welegends.common.base.BasePresenter
import es.coru.andiag.welegends.common.mvp.base.BaseView

/**
 * Created by iagocanalejas on 10/12/2016.
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {
    private val TAG = BaseActivity::class.java.simpleName

    var mPresenter: BasePresenter<BaseView>? = null

    protected abstract fun setPresenter(presenter: BasePresenter<BaseView>)

    override fun onStart() {
        super.onStart()
        if (mPresenter == null) Log.e(TAG, "Null Presenter: Initialize it on setPresenter method")
        this.mPresenter!!.attach(this)
    }

    override fun onStop() {
        super.onStop()
        this.mPresenter!!.detach()
        this.mPresenter = null
    }
}