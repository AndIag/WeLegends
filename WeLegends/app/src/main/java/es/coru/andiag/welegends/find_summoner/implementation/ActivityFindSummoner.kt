package es.coru.andiag.welegends.find_summoner.implementation

import android.os.Bundle
import butterknife.ButterKnife
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.mvp.BaseLoadingActivity
import es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner


class ActivityFindSummoner : BaseLoadingActivity() {
    private val TAG = ActivityFindSummoner::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_summoner)
        ButterKnife.bind(this)

        setProgressBar(R.id.progressBar, true)
        setPresenter(PresenterActivityFindSummoner())

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                FragmentFindSummoner(), FragmentFindSummoner.TAG)
                .commit()

    }

}
