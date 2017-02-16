package com.andiag.welegends.views.summoners.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import butterknife.BindView
import com.andiag.commons.fragments.AIButterFragment
import com.andiag.commons.fragments.FragmentLayout
import com.andiag.core.annotations.Presenter
import com.andiag.welegends.R
import com.andiag.welegends.models.database.Summoner
import com.andiag.welegends.views.adapters.AdapterSummonerList
import com.andiag.welegends.views.summoners.ActivitySummoners
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by andyq on 09/12/2016.
 */
@Presenter(presenter = PresenterSummonerList::class)
@FragmentLayout(res = R.layout.fragment_summoner_list)
class FragmentSummonerList : AIButterFragment<PresenterSummonerList>(), IViewSummonerList {

    @BindView(R.id.recyclerSummoners)
    lateinit var recycler: RecyclerView

    var adapter: AdapterSummonerList? = null

    companion object {
        val TAG: String = FragmentSummonerList::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //region Fragment Lifecycle
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(mParentContext)
        initAdapter()

    }
    //endregion

    //region View Config
    private fun initAdapter() {
        adapter = AdapterSummonerList(R.layout.item_summoner_list, ArrayList<Summoner>(), mPresenter.getVersion())
        adapter!!.emptyView = LayoutInflater.from(mParentContext).inflate(R.layout.empty_summoner_view, null)
        adapter!!.openLoadAnimation()
        recycler.adapter = adapter
        recycler.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
                startActivity((mParentContext as ActivitySummoners)
                        .createMainIntent((adapter as AdapterSummonerList)
                                .getItem(position), true))
            }
        })
        mPresenter.loadSummoners()
    }
    //endregion

    //region Callbacks
    override fun onSummonersLoaded(summoners: List<Summoner>) {
        adapter!!.addData(summoners)
    }

    override fun onSummonersEmpty(error: String?) {
        mParentContext.toast(error!!)
    }
    //endregion

}
