package com.andiag.welegends.views.summoners.search


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
import com.andiag.commons.fragments.FragmentLayout
import com.andiag.core.annotations.Presenter
import com.andiag.welegends.R
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.summoners.ActivitySummoners
import org.jetbrains.anko.toast


/**
 * Created by Canalejas on 08/12/2016.
 */
@Presenter(presenter = PresenterFindSummoner::class)
@FragmentLayout(res = R.layout.fragment_find_summoner)
class FragmentFindSummoner : AIButterFragment<PresenterFindSummoner>(), IViewFindSummoner {

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

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

    /**
     * Replace [FragmentSummonerList] in activity
     */
    @OnClick(R.id.buttonHistoric)
    fun showSummonerList() {
        (mParentContext as ActivitySummoners).onClickSwapFragment()
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(editSummonerName.windowToken, 0)
    }

    //region Fragment Lifecycle
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (mParentContext as ActivitySummoners).setBackground("Morgana_3.jpg")

        if (mPresenter.isLoadingVersion()) {
            showLoading()
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
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(editSummonerName.windowToken, 0)
        if (!mPresenter.isLoadingVersion()) {
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
    override fun onSummonerFound(summoner: Summoner, isLocal: Boolean) {
        hideLoading()
        startActivity((mParentContext as ActivitySummoners).createMainIntent(summoner, isLocal))
    }

    /**
     * Handle find summoner errors
     */
    override fun onSummonerNotFound(message: String) {
        hideLoading()
        mParentContext.toast(message)
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
    //endregion
    //endregion

}
