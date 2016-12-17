package es.coru.andiag.welegends.views.summoners.impl

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.FragmentBase
import es.coru.andiag.welegends.models.Version
import es.coru.andiag.welegends.models.wrapped.database.Summoner
import es.coru.andiag.welegends.presenters.summoners.PresenterFragmentSummonerList
import es.coru.andiag.welegends.views.adapters.AdapterSummonerList
import es.coru.andiag.welegends.views.summoners.ViewFragmentSummonerList
import java.util.*


/**
 * Created by andyq on 09/12/2016.
 */
class FragmentSummonerList : FragmentBase<PresenterFragmentSummonerList>(), ViewFragmentSummonerList {

    @BindView(R.id.recyclerSummoners)
    lateinit var recycler: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    var adapter: AdapterSummonerList? = null

    override val fragmentLayout: Int = R.layout.fragment_summoner_list

    //region Fragment Lifecycle
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = PresenterFragmentSummonerList()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(mParentContext)
        initAdapter()

        configureToolbar((mParentContext as ActivitySummoners))
    }
    //endregion

    //region View Config
    private fun configureToolbar(activity: AppCompatActivity) {
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    private fun initAdapter() {
        adapter = AdapterSummonerList(R.layout.item_summoner_list, ArrayList<Summoner>(), Version.getVersion(null)!!)
        adapter!!.emptyView = LayoutInflater.from(mParentContext).inflate(R.layout.empty_summoner_view, null)
        adapter!!.openLoadAnimation()
        recycler.adapter = adapter
        recycler.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(p0: BaseQuickAdapter<*, *>?, p1: View?, p2: Int) {
                val summoner: Summoner = p0!!.getItem(p2) as Summoner
            }

        })
        presenter.loadSummoners()
    }
    //endregion

    //region Callbacks
    override fun onSummonersLoaded(summoners: List<Summoner>) {
        adapter!!.setNewData(summoners)
    }

    override fun onSummonersEmpty(error: Int?) {
        adapter!!.setNewData(null)
        Toast.makeText(context, error!!, Toast.LENGTH_SHORT).show()
    }
    //endregion

    companion object {
        val TAG: String = FragmentSummonerList::class.java.simpleName
    }
}
