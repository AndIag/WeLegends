package es.coru.andiag.welegends.views.summoners.impl

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView
import es.coru.andiag.welegends.R
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

    fun setBackground(image: String) {
        val endpoint: String
        if (resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            endpoint = RestClient.loadingImgEndpoint + image
        } else
            endpoint = RestClient.loadingImgEndpoint + image
        Glide.with(this).load(endpoint).dontAnimate()
                .placeholder(R.drawable.background_default1).error(R.drawable.background_default1)
                .into(imageBackground)
    }

}