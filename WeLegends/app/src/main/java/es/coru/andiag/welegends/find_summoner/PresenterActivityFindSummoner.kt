package es.coru.andiag.welegends.find_summoner

import es.coru.andiag.welegends.common.base.BasePresenter


/**
 * Created by Canalejas on 08/12/2016.
 */
class PresenterActivityFindSummoner() : BasePresenter<ViewActivityFindSummoner>() {

    private val TAG = es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner::class.java.simpleName

    override fun onAttach() {
        super.onAttach()
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