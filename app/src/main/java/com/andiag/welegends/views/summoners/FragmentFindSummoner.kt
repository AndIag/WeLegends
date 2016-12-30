package com.andiag.welegends.views.summoners


import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import butterknife.BindArray
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnEditorAction
import com.andiag.commons.fragments.AIButterFragment
import com.andiag.welegends.R
import com.andiag.welegends.WeLegends
import com.andiag.welegends.models.EPVersion
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.presenters.summoners.PresenterFragmentFindSummoner
import org.jetbrains.anko.toast


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner : AIButterFragment<PresenterFragmentFindSummoner>(), ViewFragmentFindSummoner {

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
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(editSummonerName.windowToken, 0)
    }

    override fun onInitLayout() {
        mFragmentLayout = R.layout.fragment_find_summoner
    }

    override fun onInitPresenter() {
        mPresenter = PresenterFragmentFindSummoner.getInstance()
    }

    //region Fragment Lifecycle
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mParentContext as ActivitySummoners).setBackground("Morgana_3.jpg")
        WeLegends.view = view

        if (mPresenter.getServerVersion() == null) {
            showLoading()
        } else {
            textVersion.text = mPresenter.getServerVersion()
            hideLoading()
        }
        setupRegionPicker()
    }

    fun setupRegionPicker() {
        numberPicker.minValue = 0
        numberPicker.maxValue = regions.size - 1
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
    //region Find EPSummoner
    /**
     * Try to find a [Summoner] using a name and a region
     */
    @OnClick(R.id.buttonGo)
    fun findSummoner() {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(editSummonerName.windowToken, 0)
        if (!EPVersion.isLoading()) {
            showLoading()
            mPresenter!!.getSummonerByName(editSummonerName.text.toString(), region)
            return
        }
        mParentContext.toast(R.string.wait_static_data_end)
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
     * @param [summoner] retrieved summoner
     */
    override fun onSummonerFound(summoner: Summoner) {
        hideLoading()
        startActivity((mParentContext as ActivitySummoners).createMainIntent(summoner, false))
    }

    /**
     * Handle find summoner errors
     */
    override fun onSummonerNotFound(error: Int) {
        hideLoading()
        mParentContext.toast(error)
    }

    /**
     * Handle find summoner errors
     */
    override fun onSummonerNotFound(message: String) {
        hideLoading()
        mParentContext.toast(message)
    }
    //endregion

    //region EPVersion Load
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
        mParentContext.toast(error)
    }

    /**
     * Handle load version errors
     */
    override fun onVersionLoadError(error: String) {
        hideLoading()
        mParentContext.toast(error)
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
