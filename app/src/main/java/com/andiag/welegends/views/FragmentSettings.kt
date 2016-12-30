package com.andiag.welegends.views

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.preference.PreferenceFragmentCompat
import com.andiag.welegends.R
import com.andiag.welegends.common.base.ActivityBase

/**
 * Created by andyq on 24/12/2016.
 */
class FragmentSettings : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    internal var sharedPreferences: SharedPreferences? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun setDivider(divider: Drawable) {
        super.setDivider(null)
    }

    override fun onResume() {
        super.onResume()
        //unregister the preferenceChange listener
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        //unregister the preference change listener
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            ActivityBase.PREF_THEME_KEY //Change the theme
            -> activity.recreate()
        }//            case ActivityIntro.IS_FIRST_TIME_LAUNCH:
        //                Toast.makeText(getContext(),R.string.first_time_reset,Toast.LENGTH_LONG).show();
        //                //getActivity().recreate();
        //                break;
    }

    companion object {
        val TAG = "FragmentSettings"
    }
}