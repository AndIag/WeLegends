package es.coru.andiag.welegends.common.mvp.base

import android.content.Context

/**
 * Created by iagocanalejas on 10/12/2016.
 */
abstract class BasePresenter<V> where V : BaseView, V : Context {

    private val TAG = BasePresenter::class.java.simpleName

    var mView: V? = null
        get private set

    open fun attach(view: V) {
        this.mView = view
        onViewAttached()
    }

    open fun detach() {
        this.mView = null
        onViewDetached()
    }

    abstract fun onViewAttached()
    abstract fun onViewDetached()

}