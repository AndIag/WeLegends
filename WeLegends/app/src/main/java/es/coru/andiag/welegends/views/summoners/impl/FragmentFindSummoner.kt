package es.coru.andiag.welegends.views.summoners.impl


import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnEditorAction
import butterknife.OnItemSelected
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.FragmentBase
import es.coru.andiag.welegends.common.utils.FontTextView
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner
import es.coru.andiag.welegends.views.summoners.ViewFragmentFindSummoner


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : FragmentBase<PresenterFragmentFindSummoner>(), ViewFragmentFindSummoner {

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

    @OnClick(R.id.buttonGo)
    fun findSummoner() {
        presenter!!.getSummonerByName(editSummonerName.text.toString(), region)
    }

    @OnEditorAction(value = R.id.editTextSummoner)
    fun findSummoner(actionId: Int, event: KeyEvent?): Boolean {
        if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            findSummoner()
        }
        return false
    }

    @OnItemSelected(R.id.spinnerRegions)
    fun spinnerItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d(TAG, p0!!.getItemAtPosition(p2) as String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = PresenterFragmentFindSummoner()
    }

    override fun setupViews() {
        (context as ActivitySummoners).setBackground("Tristana_5.jpg")
    }

    override fun onSummonerFound(summoner: Summoner) {
        Toast.makeText(context, summoner.name + " " + summoner.mid, Toast.LENGTH_SHORT).show()
    }

    override fun onSummonerNotFound(error: Int) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun onSummonerNotFound(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun errorLoading(resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
