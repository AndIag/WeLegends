package es.coru.andiag.welegends.views.summoners.impl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.wrapped.api.RestClient


class ActivitySummoners : AppCompatActivity(), AIInterfaceActivityView {
    private val TAG = ActivitySummoners::class.java.simpleName

    @BindView(R.id.imageBackground)
    lateinit var imageBackground: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_summoner)
        ButterKnife.bind(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                FragmentFindSummoner(), FragmentFindSummoner.TAG)
                .commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        imageBackground.visibility = View.VISIBLE
    }

    /**
     * Load given image(champion splash) as activity background
     */
    fun setBackground(image: String) {
        val endpoint: String = RestClient.loadingImgEndpoint + image
        Glide.with(this).load(endpoint).dontAnimate()
                .placeholder(R.drawable.background_default1).error(R.drawable.background_default1)
                .into(imageBackground)
    }

    /**
     * If container exist and version are loaded change fragment to summoner history
     */
    fun onClickSwapFragment() {
        if (findViewById(R.id.fragmentContainer) != null && !Version.isLoading()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FragmentSummonerList(), FragmentSummonerList.TAG)
                    .addToBackStack(null)
                    .commit()

            imageBackground.visibility = View.GONE
            return
        }
        if (Version.isLoading()) {
            //Notify user data load should end
            Toast.makeText(this, R.string.wait_static_data_end, Toast.LENGTH_SHORT).show()
        }
    }

}
