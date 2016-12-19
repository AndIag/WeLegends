package es.coru.andiag.welegends.views.summoners.impl


import android.content.Context
import android.os.Bundle
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
import es.coru.andiag.welegends.WeLegends
import es.coru.andiag.welegends.common.base.FragmentBase
import es.coru.andiag.welegends.common.utils.FontTextView
import es.coru.andiag.welegends.models.Version
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

    var region: String = "EUW"
    override val fragmentLayout: Int = R.layout.fragment_find_summoner

    /**
     * Replace [FragmentSummonerList] in activity
     */
    @OnClick(R.id.buttonHistoric)
    fun showSummonerList() {
        (mParentContext as ActivitySummoners).onClickSwapFragment()
    }

    /**
     * Change picked region
     */
    @OnItemSelected(R.id.spinnerRegions)
    fun spinnerItemSelected(p0: AdapterView<*>?, p2: Int) {
        region = p0!!.getItemAtPosition(p2) as String
    }

    //region Fragment Lifecycle
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = PresenterFragmentFindSummoner.getInstance()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mParentContext as ActivitySummoners).setBackground("Tristana_5.jpg")
        WeLegends.view = view

        if (presenter.getServerVersion() == null) {
            showLoading()
        } else {
            textVersion.text = presenter.getServerVersion()
            hideLoading()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WeLegends.view = null
    }

    //endregion

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    //region Callbacks
    //region Find Summoner
    /**
     * Try to find a [Summoner] using a name and a region
     */
    @OnClick(R.id.buttonGo)
    fun findSummoner() {
        if (!Version.isLoading()) {
            showLoading()
            presenter!!.getSummonerByName(editSummonerName.text.toString(), region)
            return
        }
        Toast.makeText(context, R.string.wait_static_data_end, Toast.LENGTH_SHORT).show()
    }

    /**
     * Launch [FragmentFindSummoner.findSummoner] on keyboard press enter
     */
    @OnEditorAction(value = R.id.editTextSummoner)
    fun findSummoner(actionId: Int, event: KeyEvent?): Boolean {
        if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            findSummoner()
        }
        return false
    }

    /**
     * Launch new activity with retrieved summoner
     * @param [Summoner] retrieved summoner
     */
    override fun onSummonerFound(summoner: Summoner) {
        hideLoading()
        Log.i(TAG, "Found summoner %s with id %d".format(summoner.name, summoner.riotId))
        Toast.makeText(mParentContext, summoner.name + " " + summoner.mid, Toast.LENGTH_SHORT).show()
        // TODO launch new activity
    }

    /**
     * Handle find summoner errors
     */
    override fun onSummonerNotFound(error: Int) {
        hideLoading()
        Toast.makeText(mParentContext, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Handle find summoner errors
     */
    override fun onSummonerNotFound(message: String) {
        hideLoading()
        Toast.makeText(mParentContext, message, Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region Version Load
    /**
     * Update given version in view
     */
    override fun onVersionLoaded(version: String) {
        hideLoading()
        textVersion.text = version
    }

    /**
     * Handle load version errors
     */
    override fun onVersionLoadError(error: Int) {
        hideLoading()
        Toast.makeText(mParentContext, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Handle load version errors
     */
    override fun onVersionLoadError(error: String) {
        hideLoading()
        Toast.makeText(mParentContext, error, Toast.LENGTH_SHORT).show()
    }
    //endregion

    /**
     * Update view depending on message and loading state
     */
    override fun onStaticDataLoadChange(message: String, stillLoading: Boolean) {
        textVersion.text = message
        if (stillLoading) showLoading() else hideLoading()
    }
    //endregion

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
