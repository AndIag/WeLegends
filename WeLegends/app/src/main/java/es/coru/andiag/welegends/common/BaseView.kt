package es.coru.andiag.welegends.common

/**
 * Created by Canalejas on 08/12/2016.
 */

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun errorLoading(message: String?)
}
