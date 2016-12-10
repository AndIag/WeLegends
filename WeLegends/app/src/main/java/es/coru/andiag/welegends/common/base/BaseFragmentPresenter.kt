package es.coru.andiag.welegends.common.base

import android.content.Context
import android.support.v4.app.Fragment

/**
 * Created by iagoc on 10/12/2016.
 */
abstract class BaseFragmentPresenter<V : Fragment, C : Context> : BasePresenter<V>() {

    var parentView: C? = null
        get private set

    fun attach(view: V, parentView: C) {
        this.parentView = parentView
        super.attach(view)
    }

    override fun detach() {
        super.detach()
        this.parentView = null
    }
}