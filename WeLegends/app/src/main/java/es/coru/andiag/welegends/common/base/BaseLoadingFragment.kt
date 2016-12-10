package es.coru.andiag.welegends.common.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by andyq on 09/12/2016.
 */
abstract class BaseLoadingFragment<P : BaseFragmentPresenter<*, C>, C> : Fragment()
where C : Context, C : BaseLoadingView {

    var presenter: P? = null
        get private set
    var parentView: C? = null
        get private set

    lateinit internal var unbinder: Unbinder
    protected abstract val fragmentLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.presenter = this.addPresenter()
        this.onPresenterCreated(this.presenter!!)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.parentView = context as C
    }

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
        unbinder.unbind()
        (this.presenter as BasePresenter<*>).detach()
    }

    abstract fun setupViews()

    abstract fun addPresenter(): P

    abstract fun onPresenterCreated(p: P)
}
