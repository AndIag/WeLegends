package es.coru.andiag.welegends.views.summoners.impl


import android.content.Context
import android.view.View
import android.widget.*
import butterknife.BindView
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.FragmentBase
import es.coru.andiag.welegends.common.utils.FontTextView
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner
import es.coru.andiag.welegends.views.summoners.ViewFragmentFindSummoner


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : FragmentBase<PresenterFragmentFindSummoner>(), ViewFragmentFindSummoner, AdapterView.OnItemSelectedListener {

    @BindView(R.id.editTextSummoner)
    lateinit var editSummonerName: EditText
    @BindView(R.id.buttonGo)
    lateinit var buttonSearch: Button
    @BindView(R.id.spinnerRegions)
    lateinit var spinnerRegions: Spinner
    @BindView(R.id.buttonHistoric)
    lateinit var buttonHistoric: ImageButton
    @BindView(R.id.textVersion)
    lateinit var textVersion: FontTextView
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    lateinit var region: String
    override val fragmentLayout: Int = R.layout.fragment_find_summoner

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = PresenterFragmentFindSummoner()
    }

    override fun setupViews() {
        (context as ActivitySummoners).setBackground("Tristana_5.jpg")
        spinnerRegions.onItemSelectedListener = this
        buttonSearch.setOnClickListener {
            presenter!!.getSummonerByName(editSummonerName.text.toString(), region)
        }
    }

    override fun onSummonerFound(summoner: Summoner) {
        Toast.makeText(context, summoner.name + " " + summoner.mid, Toast.LENGTH_SHORT).show()
    }

    override fun onSummonerNotFound(error: Int) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        region = p0!!.getItemAtPosition(p2) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onVersionUpdate(version: String) {
        textVersion.text = version
    }

    override fun isLoading(): Boolean {
        return progressBar.visibility == View.VISIBLE
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun errorLoading(message: String?) {
    }

    override fun errorLoading(stringResource: Int) {
    }

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
