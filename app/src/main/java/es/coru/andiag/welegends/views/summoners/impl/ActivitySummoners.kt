package es.coru.andiag.welegends.views.summoners.impl

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.graphics.ColorUtils
import android.view.MenuItem
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //endregion

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
