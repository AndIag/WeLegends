package es.coru.andiag.welegends.find

import es.coru.andiag.welegends.common.LoadingView

/**
 * Created by Canalejas on 08/12/2016.
 */

interface InitView : LoadingView {
    fun updateVersion(version: String)
    fun updateBackground(imageUrl: String)
}
