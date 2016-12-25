package es.coru.andiag.welegends.views.summoners.impl


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.BindArray
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnEditorAction
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.WeLegends
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner
import es.coru.andiag.welegends.views.main.ActivityMain
import es.coru.andiag.welegends.views.summoners.ViewFragmentFindSummoner
import es.coru.andoiag.andiag_mvp_utils.fragments.AIButterFragment


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : AIButterFragment<PresenterFragmentFindSummoner>(), ViewFragmentFindSummoner {

    @BindView(R.id.editTextSummoner)
    lateinit var editSummonerName: EditText
    @BindView(R.id.buttonGo)
    lateinit var buttonSearch: ImageButton
    @BindView(R.id.buttonHistoric)
    lateinit var buttonHistoric: ImageButton
    @BindView(R.id.textVersion)
    lateinit var textVersion: TextView
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar
    @BindArray(R.array.region_array)
    lateinit var regions: Array<String>
    @BindView(R.id.number_picker)
    lateinit var numberPicker: NumberPicker

    var region: String = "EUW"

    /**
     * Replace [FragmentSummonerList] in activity
     */
    @OnClick(R.id.buttonHistoric)
    fun showSummonerList() {
        (mParentContext as ActivitySummoners).onClickSwapFragment()
    }

    override fun initLayout() {
        mFragmentLayout = R.layout.fragment_find_summoner
    }

    override fun initPresenter() {
        mPresenter = PresenterFragmentFindSummoner.getInstance()
    }

    //region Fragment Lifecycle
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mParentContext as ActivitySummoners).setBackground("Morgana_3.jpg")
        WeLegends.view = view

//        editSummonerName.typeface = Typeface.createFromAsset(mParentContext.assets, "Aller.ttf")

        if (mPresenter.getServerVersion() == null) {
            showLoading()
        } else {
            textVersion.text = mPresenter.getServerVersion()
            hideLoading()
        }
        setupRegionPicker()
    }

    fun setupRegionPicker(){
        numberPicker.minValue = 0
        numberPicker.maxValue = regions.size-1
        numberPicker.displayedValues = regions
        numberPicker.wrapSelectorWheel = true
        numberPicker.setOnValueChangedListener({ numberPicker, any, i ->
            region = regions[i]
        })
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
            mPresenter!!.getSummonerByName(editSummonerName.text.toString(), region)
            return
        }
        Toast.makeText(mParentContext, R.string.wait_static_data_end, Toast.LENGTH_SHORT).show()
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
        startActivity(Intent(mParentContext, ActivityMain::class.java))
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
    override fun onStaticDataLoadChange(message: String?) {
        textVersion.text = message
    }
    //endregion

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
