package es.coru.andiag.welegends.views.summoners.impl

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.graphics.ColorUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hariofspades.gradientartist.GradientArtistBasic
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.ActivityBase
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.wrapped.api.RestClient
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.views.main.ActivityMain


class ActivitySummoners : ActivityBase() {
    private val TAG = ActivitySummoners::class.java.simpleName

    @BindView(R.id.imageBackground)
    lateinit var imageBackground: GradientArtistBasic

    //region Activity Lifecycle
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
    //endregion

    /**
     * Create a new [ActivityMain] [Intent]
     * @param [summoner] found [Summoner]
     * @param [isLocal] boolean indicating if the summoner was load from local database
     */
    fun createMainIntent(summoner: Summoner, isLocal: Boolean): Intent {
        val intent: Intent = Intent(this, ActivityMain::class.java)
        intent.putExtra(ActivityMain.VAL_SUMMONER_ID, summoner.mid)
        intent.putExtra(ActivityMain.VAL_SUMMONER_RIOT_ID, summoner.riotId)
        intent.putExtra(ActivityMain.VAL_SUMMONER_LVL, summoner.summonerLevel)
        intent.putExtra(ActivityMain.CONF_SEARCH_REQUIRED, isLocal)
        return intent
    }

    /**
     * Load given image(champion splash) as activity background
     */
    fun setBackground(image: String) {
        val endpoint: String = RestClient.splashImgEndpoint + image
        imageBackground.setUrlImage(endpoint,R.drawable.background_default,R.drawable.background_default, ImageView.ScaleType.CENTER_CROP)
        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(ColorUtils.setAlphaComponent(resolveColorAttribute(this,R.attr.colorPrimaryDark),0xD9),
                        ColorUtils.setAlphaComponent(resolveColorAttribute(this,R.attr.colorAccent),0xB3)))
        imageBackground.setGradient(gd)
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
