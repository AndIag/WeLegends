package andiag.coru.es.welegends.activities;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;

/**
 * Created by Iago on 12/07/2015.
 */
public class TabbedActivity extends ActionBarActivity {

    protected ArrayList<Tab> tabs = new ArrayList<>();
    protected ViewPager mPager;
    protected SectionsPagerAdapter mPagerAdapter;

    protected void createTabs() {
    }

    protected void setPager() {
        mPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
    }

    protected class Tab {

        private Fragment fragment;
        private String name = "DEFAULT";
        private int actionBarColors = Color.CYAN;
        private int toolBarColors = Color.CYAN;

        public Tab() {
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            if (fragment != null) {
                this.fragment = fragment;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (name != null) {
                this.name = name;
            }
        }

        public int getActionBarColors() {
            return actionBarColors;
        }

        public void setActionBarColors(int actionBarColors) {
            this.actionBarColors = actionBarColors;
        }

        public int getToolBarColors() {
            return toolBarColors;
        }

        public void setToolBarColors(int toolBarColors) {
            this.toolBarColors = toolBarColors;
        }
    }

    protected class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int DEFAULT_COLOR = Color.CYAN;
        private final String DEFAULT_NAME = "DEFAULT";

        private ArrayList<Tab> tabs = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Tab> tabs) {
            super(fm);
            this.tabs = tabs;
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return tabs.get(position).getFragment();
            } catch (IndexOutOfBoundsException e) {
                return new Fragment();
            }
        }

        public int getColorActionBar(int position) {
            try {
                return tabs.get(position).getActionBarColors();
            } catch (IndexOutOfBoundsException e) {
                return DEFAULT_COLOR;
            }
        }

        public int getColorToolBar(int position) {
            try {
                return tabs.get(position).getToolBarColors();
            } catch (IndexOutOfBoundsException e) {
                return DEFAULT_COLOR;
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return tabs.get(position).getName();
            } catch (IndexOutOfBoundsException e) {
                return DEFAULT_NAME;
            }
        }

        public Fragment getItemAt(int currentItem) {
            return getItem(currentItem);
        }
    }
}
