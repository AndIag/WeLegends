package es.coru.andiag.welegends.find

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import butterknife.ButterKnife
import es.coru.andiag.welegends.R


class ActivityInit : AppCompatActivity() {

    private val TAG = "ActivityInit"
    private val presenter: InitPresenter = InitPresenter.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        ButterKnife.bind(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    fun showLoading() {
        Log.i(TAG, "showLoading")
    }

    fun hideLoading() {
        Log.i(TAG, "hideLoading")
    }

}
