package es.coru.andiag.welegends.common.mvp

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import es.coru.andiag.welegends.common.mvp.base.BaseFragmentPresenter
import es.coru.andiag.welegends.common.mvp.base.BaseFragmentView
import es.coru.andiag.welegends.common.mvp.base.BaseView

/**
 * Created by iagocanalejas on 10/12/2016.
 */
abstract class BaseFragment<A> : Fragment(), BaseFragmentView where A : Context, A : BaseView {
    private val TAG = BaseFragment::class.java.simpleName

    var mPresenter: BaseFragmentPresenter<BaseFragment<*>, A>? = null
    var mParentContext: A? = null

    protected abstract fun setPresenter(presenter: BaseFragmentPresenter<BaseFragment<*>, A>)

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mParentContext = context as A

        if (mPresenter == null) Log.e(TAG, "Null Presenter: Initialize it on setPresenter method")
        this.mPresenter!!.attach(this, this.mParentContext!!)
    }

    override fun onDetach() {
        super.onDetach()
        this.mPresenter!!.detach()
        this.mPresenter = null
    }
}