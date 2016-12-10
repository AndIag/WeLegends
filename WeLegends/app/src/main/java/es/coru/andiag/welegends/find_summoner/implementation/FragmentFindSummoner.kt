package es.coru.andiag.welegends.find_summoner.implementation


import android.content.res.Configuration
import android.view.View
import android.widget.*
import butterknife.BindView
import com.bumptech.glide.Glide
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.BaseLoadingFragment
import es.coru.andiag.welegends.common.utils.FontTextView
import es.coru.andiag.welegends.find_summoner.PresenterFragmentFindSummoner
import es.coru.andiag.welegends.find_summoner.ViewFragmentFindSummoner
import es.coru.andiag.welegends.models.entities.database.Summoner
import es.coru.andiag.welegends.models.rest.RestClient


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : BaseLoadingFragment<PresenterFragmentFindSummoner, ActivityFindSummoner>(), ViewFragmentFindSummoner, AdapterView.OnItemSelectedListener {

    @BindView(R.id.editTextSummoner)
    lateinit var editSummonerName: EditText
    @BindView(R.id.buttonGo)
    lateinit var buttonSearch: Button
    @BindView(R.id.imageBackground)
    lateinit var imageBackground: ImageView
    @BindView(R.id.spinnerRegions)
    lateinit var spinnerRegions: Spinner
    @BindView(R.id.buttonHistoric)
    lateinit var buttonHistoric: ImageButton
    @BindView(R.id.textVersion)
    lateinit var textVersion: FontTextView

    lateinit var region: String

    override val fragmentLayout: Int
        get() = R.layout.fragment_find_summoner

    override fun setupViews() {
        setBackground("Tristana_5.jpg")
        spinnerRegions.onItemSelectedListener = this
        buttonSearch.setOnClickListener {
            presenter!!.getSummonerByName(editSummonerName.text.toString(), region)
        }
    }

    override fun addPresenter(): PresenterFragmentFindSummoner {
        return PresenterFragmentFindSummoner()
    }

    override fun onPresenterCreated(p: PresenterFragmentFindSummoner) {
        p.attach(this, parentView!!)
    }

    override fun onSummonerFound(summoner: Summoner) {
        Toast.makeText(context, summoner.name + " " + summoner.id, Toast.LENGTH_SHORT).show()
    }

    override fun onSummonerNotFound(error: Int) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    fun updateVersion(version: String) {
        textVersion.text = version
    }

    private fun setBackground(image: String) {
        val endpoint: String
        if (resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            endpoint = RestClient.loadingImgEndpoint + image
        } else
            endpoint = RestClient.loadingImgEndpoint + image
        Glide.with(context).load(endpoint).dontAnimate()
                .placeholder(R.drawable.background_default1).error(R.drawable.background_default1)
                .into(imageBackground)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        region = p0!!.getItemAtPosition(p2) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onVersionUpdate(version: String) {
        textVersion.text = version
    }

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
