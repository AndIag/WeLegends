package es.coru.andiag.welegends.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import es.coru.andiag.andiag_mvp.BaseFragment
import es.coru.andiag.andiag_mvp.base.BaseFragmentPresenter

/**
 * Created by Canalejas on 11/12/2016.
 */
abstract class ButterFragment<P> : BaseFragment<P>() where P : BaseFragmentPresenter<*, *> {

    internal var unbinder: Unbinder? = null
    protected abstract val fragmentLayout: Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(fragmentLayout, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
        unbinder = null
    }

    abstract fun setupViews()

}