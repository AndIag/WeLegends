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
abstract class BaseLoadingFragment<P : BasePresenter<*>, A : BaseLoadingView>() : Fragment() {

    lateinit var parentView: A

    lateinit internal var unbinder: Unbinder
    var presenter: P? = null
        protected set
    protected abstract val fragmentLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.presenter = this.addPresenter()
        this.onPresenterCreated(this.presenter!!)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(activity: Context?) {
        super.onAttach(activity)
        this.parentView = activity as A
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
        if (this.presenter != null) {
            (this.presenter as BasePresenter<*>).detach()
        }
    }

    abstract fun setupViews()

    abstract fun addPresenter(): P

    abstract fun onPresenterCreated(var1: P)
}
