package es.coru.andiag.welegends.common.mvp

/**
 * Created by iagocanalejas on 10/12/2016.
 */
interface BaseLoadingView {
    fun showLoading()
    fun hideLoading()
    fun errorLoading(message: String?)
}