package es.coru.andiag.welegends.common

/**
 * Created by Canalejas on 08/12/2016.
 */

interface Presenter<V> {
    fun onViewAttached(view: V)
    fun onViewDetached()
}
