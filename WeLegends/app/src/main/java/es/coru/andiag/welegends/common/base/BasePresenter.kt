package es.coru.andiag.welegends.common.base

/**
 * Created by Canalejas on 08/12/2016.
 */


abstract class BasePresenter<V> {

    var view: V? = null
        get private set

    open fun attach(view: V) {
        this.view = view
        onAttach()
    }

    open fun onAttach() {
    }

    open fun detach() {
        onDetach()
        view = null
    }

    open fun onDetach() {
    }

}
