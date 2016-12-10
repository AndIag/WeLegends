package es.coru.andiag.welegends.find_summoner.implementation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.BuildConfig
import butterknife.ButterKnife
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.find_summoner.PresenterActivityFindSummoner
import es.coru.andiag.welegends.find_summoner.ViewActivityFindSummoner


class ActivityFindSummoner : AppCompatActivity(), ViewActivityFindSummoner {

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    private val TAG = ActivityFindSummoner::class.java.simpleName
    private val presenter: PresenterActivityFindSummoner = PresenterActivityFindSummoner.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        ButterKnife.setDebug(BuildConfig.DEBUG)
        ButterKnife.bind(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                FragmentFindSummoner(), FragmentFindSummoner.TAG)
                .commit()

    }

    override fun onStart() {
        super.onStart()
        presenter.attach(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.attach(this)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun errorLoading(message: String?) {

    }

}
