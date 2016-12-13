package es.coru.andiag.welegends.views.find_summoner.impl


import android.content.Context
import android.view.View
import android.widget.*
import butterknife.BindView
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.ButterFragment
import es.coru.andiag.welegends.common.utils.FontTextView
import es.coru.andiag.welegends.models.entities.Summoner
import es.coru.andiag.welegends.presenters.PresenterFragmentFindSummoner
import es.coru.andiag.welegends.views.find_summoner.ViewFragmentFindSummoner


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : ButterFragment<PresenterFragmentFindSummoner>(), ViewFragmentFindSummoner, AdapterView.OnItemSelectedListener {

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

    lateinit var region: String
    override val fragmentLayout: Int = R.layout.fragment_find_summoner

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = PresenterFragmentFindSummoner()
    }

    override fun setupViews() {
        (context as ActivityFindSummoner).setBackground("Tristana_5.jpg")
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

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
