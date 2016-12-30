package com.andiag.welegends.views.main

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindArray
import butterknife.BindView
import butterknife.ButterKnife
import com.andiag.welegends.R
import com.andiag.welegends.common.base.ActivityBase
import com.andiag.welegends.models.entities.Summoner
import com.andiag.welegends.views.FragmentSettings
import com.andiag.welegends.views.adapters.AdapterNavDrawer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import java.util.*

class ActivityMain : ActivityBase() {
    private val TAG: String = ActivityMain::class.java.simpleName

    companion object {
        val CONF_SEARCH_REQUIRED: String = "is_searched"
        val VAL_SUMMONER_ID: String = "summoner_id"
        val VAL_SUMMONER_RIOT_ID: String = "summoner_riot_id"
        val VAL_SUMMONER_LVL: String = "summoner_lvl"
        val VAL_REGION: String = "region"
    }

    private val BACK_STACK_ROOT_TAG = "ROOT_FRAGMENT"

    @BindView(R.id.toolbar_title)
    lateinit var toolbarTitle: TextView
    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.imageSummoner)
    lateinit var imageSummoner: ImageView
    @BindView(R.id.textSummoner)
    lateinit var textSummoner: TextView
    @BindView(R.id.drawer_list)
    lateinit var drawerList: RecyclerView
    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout
    @BindView(R.id.nav_view)
    lateinit var navView: NavigationView
    @BindArray(R.array.DrawerItems)
    lateinit var drawerTitles: Array<String>

    internal var toggle: ActionBarDrawerToggle? = null
    internal var adapter: AdapterNavDrawer? = null
    internal var pendingRunnable: Runnable? = null
    internal var handler = Handler()

    private var summoner: Summoner? = null

    private fun initializeViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle = object : ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val xPositionOpenDrawer = navView.width.toFloat()
                val xPositionWindowContent = slideOffset * xPositionOpenDrawer
                getActionBarView().x = xPositionWindowContent
            }

            override fun onDrawerClosed(drawerView: View?) {
                super.onDrawerClosed(drawerView)
                if (pendingRunnable != null) {
                    handler.post(pendingRunnable)
                    pendingRunnable = null
                }
            }
        }
        drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
        initializeDrawer()
    }

    private fun initializeDrawer() {
        drawerList.layoutManager = LinearLayoutManager(this)
        val items = ArrayList<com.andiag.welegends.views.adapters.items.ItemNavDrawer>()
        items.add(com.andiag.welegends.views.adapters.items.ItemNavDrawer(drawerTitles[0], R.drawable.ic_search))
        items.add(com.andiag.welegends.views.adapters.items.ItemNavDrawer(drawerTitles[1], R.drawable.ic_search))
        items.add(com.andiag.welegends.views.adapters.items.ItemNavDrawer(drawerTitles[2], R.drawable.ic_search))
        items.add(com.andiag.welegends.views.adapters.items.ItemNavDrawer(drawerTitles[3], R.drawable.ic_search))
        drawerList.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(baseQuickAdapter: BaseQuickAdapter<*, *>, view: View, i: Int) {
                selectItem(i)
            }
        })
        adapter = AdapterNavDrawer(R.layout.item_nav, items)
        adapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        drawerList.adapter = adapter
    }

    private fun selectItem(position: Int) {
        Log.d(TAG, "SELECTING POSITION: " + position)

        adapter!!.setSelected(position)
        // Update the main content by replacing fragments
        pendingRunnable = Runnable {
            //            val fragment: Fragment
//            when (position) {
//                else -> fragment = FragmentBlank.newInstance("" + position)
//            }
//            addFragmentRoot(fragment)
        }

        drawerLayout.closeDrawer(GravityCompat.START)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initializeViews()

        if (intent != null) {
            if (intent.getBooleanExtra(CONF_SEARCH_REQUIRED, true)) {
                // TODO
            }
        }

        if (savedInstanceState == null) {
            pendingRunnable = Runnable {
//                val fragment = FragmentBlank.newInstance("0")
//                addFragmentRoot(fragment)
            }
            handler.post(pendingRunnable)
        }

    }

    fun getActionBarView(): View {
        val window = this.window
        val decorView = window.decorView
        val packageName = this.packageName
        val resId = this.resources.getIdentifier("action_bar_container", "id", packageName)
        val actionBarView = decorView.findViewById(resId)
        return actionBarView
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            addFragmentOnTop(FragmentSettings())
            title = getString(R.string.action_settings)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun addFragmentOnTop(fragment: Fragment) {
        val name: String? = null
        displayBackStack(supportFragmentManager)
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frame_container, fragment)
                .addToBackStack(name)
                .commit()
    }

    private fun addFragmentRoot(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        displayBackStack(fragmentManager)
        // Empty the backstack
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        // Add the new root fragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit()
    }

    private inner class SmoothActionBarDrawerToggle(activity: Activity, drawerLayout: DrawerLayout, toolbar: Toolbar, openDrawerContentDescRes: Int, closeDrawerContentDescRes: Int) : ActionBarDrawerToggle(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes) {
        private var runnable: Runnable? = null

        override fun onDrawerOpened(drawerView: View?) {
            super.onDrawerOpened(drawerView)
            invalidateOptionsMenu()
        }

        override fun onDrawerClosed(view: View?) {
            super.onDrawerClosed(view)
            invalidateOptionsMenu()
        }

        override fun onDrawerStateChanged(newState: Int) {
            super.onDrawerStateChanged(newState)
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable!!.run()
                runnable = null
            }
        }

        fun runWhenIdle(runnable: Runnable) {
            this.runnable = runnable
        }
    }
}
