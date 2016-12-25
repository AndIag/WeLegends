package es.coru.andiag.welegends.common.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import es.coru.andiag.welegends.R
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper



/**
 * Created by andyq on 24/12/2016.
 */
abstract class ActivityBase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Updating theme from OnCreate")
        updateTheme()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun updateTheme() { //Change theme to all activities that extends BaseActivity
        if (getTheme(applicationContext) == THEME_DARK) {
            setTheme(R.style.AppTheme_Dark)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.darkPrimaryDark)
            }
            return
        }
        if (getTheme(applicationContext) == THEME_LIGHT) {
            setTheme(R.style.AppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.lightPrimaryDark)
            }
            return
        }
        if (getTheme(applicationContext) == THEME_ALTERNATE) {
            setTheme(R.style.AppTheme_Alternate)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.alternatePrimaryDark)
            }
            return
        }
        setTheme(R.style.AppTheme)
    }

    companion object {

        val TAG = ActivityBase::class.java.simpleName
        val PREF_THEME_KEY = "theme_list"

        val THEME_LIGHT = 0
        val THEME_DARK = 1
        val THEME_ALTERNATE = 2

        fun getTheme(context: Context): Int {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return Integer.valueOf(prefs.getString(PREF_THEME_KEY, "-1"))!!
        }

        fun displayBackStack(fm: FragmentManager) {
            val count = fm.backStackEntryCount
            Log.d("Backstack log", "There are $count entries")
            for (i in 0..count - 1) {
                // Display Backstack-entry data like
                val name = fm.getBackStackEntryAt(i).name
                Log.d("Backstack log", "entry $i: $name")
            }
        }

        fun resolveColorAttribute(context: Context, attribute: Int) : Int{
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attribute, typedValue, true)
            return typedValue.data
        }
    }
}