package andiag.coru.es.welegends.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

/**
 * Created by Iago on 11/07/2015.
 */
public abstract class TabbedActivity extends ActionBarActivity {

    protected SectionsPagerAdapter mPagerAdapter;

    protected abstract void setmPagerAdapter();

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> tabNames;
        private int[] actionBarColors;
        private int[] toolBarColors;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
                                    ArrayList<String> tabNames, int[] actionBarColors, int[] toolBarColors) {
            super(fm);
            this.fragments = fragments;
            this.tabNames = tabNames;
            this.toolBarColors = toolBarColors;
            this.actionBarColors = actionBarColors;
        }

        public void setActionBarColors(int[] actionBarColors) {
            this.actionBarColors = actionBarColors;
        }

        public void setToolBarColors(int[] toolBarColors) {
            this.toolBarColors = toolBarColors;
        }

        public void setTabNames(ArrayList<String> tabNames) {
            this.tabNames = tabNames;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public int getColorActionBar(int position) {

            if ((actionBarColors != null) && (actionBarColors.length > position)) {
                return actionBarColors[position];
            }
            return 0;
        }

        public int getColorToolBar(int position) {
            if ((toolBarColors != null) && (toolBarColors.length > position)) {
                return toolBarColors[position];
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
