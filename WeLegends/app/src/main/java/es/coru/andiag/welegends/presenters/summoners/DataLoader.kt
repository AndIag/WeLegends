package es.coru.andiag.welegends.presenters.summoners

import android.content.Context

/**
 * Created by Canalejas on 14/12/2016.
 */
interface DataLoader {

    fun getContext(): Context
    fun onLoadSuccess(data: String)
    fun onLoadProgressChange(message: String)
    fun onLoadError(message: String?)

}