package com.andiag.welegends.views.summoners

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import butterknife.BindView
import com.andiag.commons.fragments.AIButterFragment
import com.andiag.welegends.R
import com.andiag.welegends.models.EPVersion
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.presenters.summoners.PresenterFragmentSummonerList
import com.andiag.welegends.views.adapters.AdapterSummonerList
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by andyq on 09/12/2016.
 */
class FragmentSummonerList : AIButterFragment<PresenterFragmentSummonerList>(), ViewFragmentSummonerList {

    @BindView(R.id.recyclerSummoners)
    lateinit var recycler: RecyclerView

    var adapter: AdapterSummonerList? = null

    //region Fragment Lifecycle
    override fun onInitLayout() {
        mFragmentLayout = R.layout.fragment_summoner_list
    }

    override fun onInitPresenter() {
        mPresenter = PresenterFragmentSummonerList.getInstance()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(mParentContext)
        initAdapter()

    }
    //endregion

    //region View Config
    private fun initAdapter() {
        adapter = AdapterSummonerList(R.layout.item_summoner_list, ArrayList<Summoner>(), EPVersion.getVersion(null)!!)
        adapter!!.emptyView = LayoutInflater.from(mParentContext).inflate(R.layout.empty_summoner_view, null)
        adapter!!.openLoadAnimation()
        recycler.adapter = adapter
        recycler.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
                /**
                 * Launch [ActivityMain] on list element click
                 */
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
        adapter!!.setNewData(summoners)
    }

    override fun onSummonersEmpty(error: Int?) {
        adapter!!.setNewData(null)
        mParentContext.toast(error!!)
    }
    //endregion

    companion object {
        val TAG: String = FragmentSummonerList::class.java.simpleName
    }
}
