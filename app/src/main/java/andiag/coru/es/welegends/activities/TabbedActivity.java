package andiag.coru.es.welegends.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

/**
 * Created by Iago on 11/07/2015.
 */
public class TabbedActivity extends ActionBarActivity {

    protected ViewPager mPager;
    protected SectionsPagerAdapter mPagerAdapter;

    protected void addFragment(Fragment fragment, String name, Integer actionBarColor, Integer toolBarColor) {
        if (mPagerAdapter != null) {
            mPagerAdapter.addFragment(fragment, name, actionBarColor, toolBarColor);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
    }

    /*------------------------------------------------------------------*/
    protected class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int DEFAULT_COLOR = Color.CYAN;
        private final String DEFAULT_NAME = "DEFAULT";
        private int position;

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private ArrayList<String> tabNames = new ArrayList<>();
        private ArrayList<Integer> actionBarColors = new ArrayList<>();
        private ArrayList<Integer> toolBarColors = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            position = 0;
        }

        private void addFragment(Fragment fragment, String name, Integer actionBarColor, Integer toolBarColor) {
            if (fragment != null) {
                fragments.add(position, fragment);
                if (name != null) {
                    tabNames.add(position, name);
                } else {
                    tabNames.add(position, DEFAULT_NAME);
                }
                if (actionBarColor != null) {
                    actionBarColors.add(position, actionBarColor);
                } else {
                    actionBarColors.add(position, DEFAULT_COLOR);
                }
                if (toolBarColor != null) {
                    toolBarColors.add(position, toolBarColor);
                } else {
                    toolBarColors.add(position, DEFAULT_COLOR);
                }
                position++;
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public int getColorActionBar(int position) {
            if ((actionBarColors != null)/* && (actionBarColors.length > position)*/) {
                //return actionBarColors[position];
                return actionBarColors.get(position);
            }
            return 0;
        }

        public int getColorToolBar(int position) {
            if ((toolBarColors != null)/* && (toolBarColors.length > position)*/) {
                //return toolBarColors[position];
                return toolBarColors.get(position);
            }
            return 0;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }

        public Fragment getItemAt(int currentItem) {
            return getItem(currentItem);
        }
    }

}
