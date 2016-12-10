package es.coru.andiag.welegends.common.base

/**
 * Created by Canalejas on 08/12/2016.
 */

interface BaseLoadingView {
    fun showLoading()
    fun hideLoading()
    fun errorLoading(message: String?)
}
