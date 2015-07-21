package andiag.coru.es.welegends.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;

import andiag.coru.es.welegends.R;

/**
 * Created by Iago on 11/07/2015.
 */
public abstract class SwipeRefreshLayoutFragment extends Fragment {

    int[] colors = {R.color.swype_1, R.color.swype_2, R.color.swype_3, R.color.swype_4};
    private SwipeRefreshLayout refreshLayout;

    public SwipeRefreshLayoutFragment() {
    }

    protected abstract void initializeRefresh(View view);

    public void setColors(int[] colors) {
        if ((colors != null) && (colors.length == this.colors.length)) {
            this.colors = colors;
        }
    }

    public void setRefreshLayout(SwipeRefreshLayout view) {
        refreshLayout = view;
        refreshLayout.setColorSchemeResources(colors);
        refreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(listener);
        }
    }

    public void changeRefreshingValue(boolean bool) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(bool);
        }
    }

    public boolean isLoading() {
        if (refreshLayout != null) {
            return refreshLayout.isRefreshing();
        }
        return false;
    }

}
