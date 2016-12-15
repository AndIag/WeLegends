package es.coru.andiag.welegends.views.summoners.impl

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import es.coru.andiag.welegends.R
import es.coru.andiag.welegends.common.base.FragmentBase
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
    var recycler: RecyclerView? = null

    var adapter: AdapterSummonerList? = null

    override val fragmentLayout: Int = R.layout.fragment_summoner_list

    override fun setupViews() {
        recycler!!.setHasFixedSize(true)
        recycler!!.layoutManager = LinearLayoutManager(context)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = AdapterSummonerList(R.layout.item_summoner_historic, ArrayList<Summoner>())
        adapter!!.openLoadAnimation()
        recycler!!.adapter = adapter
        recycler!!.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(p0: BaseQuickAdapter<*, *>?, p1: View?, p2: Int) {
                val summoner : Summoner = p0!!.getItem(p2) as Summoner
            }

        })

    }

    override fun onSummonersLoaded(summoners: List<Summoner>) {
        adapter!!.setNewData(summoners)
    }

    override fun onSummonersEmpty(error: Int?) {
        adapter!!.setNewData(null)
        Toast.makeText(context, error!!, Toast.LENGTH_SHORT).show()
    }
}
