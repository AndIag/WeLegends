package es.coru.andiag.welegends.common.base

/**
 * Created by Canalejas on 08/12/2016.
 */


abstract class BasePresenter<V> {

    private var view: V? = null

    fun attach(view: V) {
        this.view = view
        onAttach()
    }

    open fun onAttach() {
    }

    fun detach() {
        onDetach()
        view = null
    }

    open fun onDetach() {
    }

    protected fun getView() = view!!

}
