package es.coru.andiag.welegends.find


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.FontTextView


/**
 * Created by Canalejas on 08/12/2016.
 */

class FragmentFindSummoner() : Fragment() {

    @BindView(R.id.textVersion) lateinit var textVersion: FontTextView

    private lateinit var bind: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_find_summoner, container, false)
        bind = ButterKnife.bind(this, view)
        return view
    }

    fun updateVersion(version: String) {
        textVersion.text = version
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind.unbind()
    }

    companion object {
        val TAG: String = FragmentFindSummoner::class.java.simpleName
    }

}
