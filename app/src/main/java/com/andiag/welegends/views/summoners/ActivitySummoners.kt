package com.andiag.welegends.views.summoners

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.graphics.ColorUtils
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.andiag.welegends.R
import com.andiag.welegends.common.base.ActivityBase
import com.andiag.welegends.models.IVersionRepository
import com.andiag.welegends.models.VersionRepository
import com.andiag.welegends.models.api.RestClient
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.main.ActivityMain
import com.andiag.welegends.views.summoners.find.FragmentFindSummoner
import com.andiag.welegends.views.summoners.historic.FragmentSummonerList
import com.hariofspades.gradientartist.GradientArtistBasic
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop


class ActivitySummoners : ActivityBase() {
    private val TAG = ActivitySummoners::class.java.simpleName
    private val VERSION_REPOSITORY: IVersionRepository = VersionRepository.getInstance()

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
        return intentFor<ActivityMain>(ActivityMain.VAL_SUMMONER_ID to summoner.mid,
                ActivityMain.VAL_SUMMONER_RIOT_ID to summoner.riotId,
                ActivityMain.VAL_SUMMONER_NAME to summoner.name,
                ActivityMain.VAL_SUMMONER_REGION to summoner.region,
                ActivityMain.CONF_SEARCH_REQUIRED to isLocal).singleTop()
    }

    /**
     * Load given image(champion splash) as activity background
     */
    fun setBackground(image: String) {
        val endpoint: String = RestClient.splashImgEndpoint + image
        imageBackground.setUrlImage(endpoint, R.drawable.background_default, R.drawable.background_default, ImageView.ScaleType.CENTER_CROP)
        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(ColorUtils.setAlphaComponent(resolveColorAttribute(this, R.attr.colorPrimaryDark), 0xD9),
                        ColorUtils.setAlphaComponent(resolveColorAttribute(this, R.attr.colorAccent), 0xB3)))
        imageBackground.setGradient(gd)
    }

    /**
     * If container exist and version are loaded change fragment to summoner history
     */
    fun onClickSwapFragment() {
        if (findViewById(R.id.fragmentContainer) != null && VERSION_REPOSITORY.getVersion() != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FragmentSummonerList(), FragmentSummonerList.TAG)
                    .addToBackStack(null)
                    .commit()

            imageBackground.visibility = View.GONE
            return
        }
        if (VERSION_REPOSITORY.getVersion() == null) {
            //Notify user data load should end
            Snackbar.make(findViewById(android.R.id.content), R.string.wait_static_data_end, Snackbar.LENGTH_SHORT)
        }
    }

}
