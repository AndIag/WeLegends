package es.coru.andiag.welegends.find_summoner

import android.content.Context


/**
 * Created by Canalejas on 08/12/2016.
 */
class PresenterActivityFindSummoner() {

    private val TAG = es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner::class.java.simpleName

    fun attach(context: Context) {
        //super.onAttach()
    }

    companion object {

        private var presenter: es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner? = null

        val instance: es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner
            get() {
                if (presenter == null) {
                    presenter = PresenterActivityFindSummoner()
                }
                return presenter!!
            }

    }
}