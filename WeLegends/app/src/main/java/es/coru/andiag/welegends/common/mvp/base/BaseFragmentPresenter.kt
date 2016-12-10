package es.coru.andiag.welegends.common.mvp.base

import android.content.Context
import android.support.v4.app.Fragment

/**
 * Created by iagocanalejas on 10/12/2016.
 */
abstract class BaseFragmentPresenter<V, PV> : BasePresenter<PV>()
where PV : Context, PV : BaseView, V : Fragment, V : BaseFragmentView {

    private val TAG = BaseFragmentPresenter::class.java.simpleName

    var mFragmentView: V? = null
        get private set

    fun attach(view: V, parent: PV) {
        mFragmentView = view
        super.attach(parent)
    }

    override fun detach() {
        mFragmentView = null
        super.detach()
    }

    override abstract fun onViewAttached()
    override abstract fun onViewDetached()

}