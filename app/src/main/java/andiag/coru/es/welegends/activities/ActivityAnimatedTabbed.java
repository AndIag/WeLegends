package andiag.coru.es.welegends.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import andiag.coru.es.welegends.R;

/**
 * Created by andyq on 28/06/2016.
 */
public class ActivityAnimatedTabbed extends ActivityTabbed {

    private ActionBar actionBar;
    private View mToolbarView;
    private SmartTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setAnimation(){
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mToolbarView = findViewById(R.id.toolbar);

        slidingTabLayout = (SmartTabLayout) findViewById(R.id.sliding_tabs);
        //slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white_20));
        slidingTabLayout.setViewPager(mPager);
        slidingTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
    }

    protected void reloadActionBar() {
        if (slidingTabLayout != null) {
            slidingTabLayout.setViewPager(mPager);
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }
}
