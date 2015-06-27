package andiag.coru.es.welegends.adapters;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import jp.wasabeef.recyclerview.animators.internal.ViewHelper;

/**
 * Created by Andy on 27/06/2015.
 */
public abstract class AnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean isFirstOnly = true;

    public AnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.mAdapter = adapter;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mAdapter.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.mAdapter.onBindViewHolder(holder, position);
        if(this.isFirstOnly && position <= this.mLastPosition) {
            ViewHelper.clear(holder.itemView);
        } else {
            Animator[] var3 = this.getAnimators(holder.itemView);
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Animator anim = var3[var5];
                anim.setDuration((long)this.mDuration).start();
                anim.setInterpolator(this.mInterpolator);
            }

            this.mLastPosition = position;
        }

    }

    public int getItemCount() {
        return this.mAdapter.getItemCount();
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        this.mLastPosition = start;
    }

    protected abstract Animator[] getAnimators(View var1);

    public void setFirstOnly(boolean firstOnly) {
        this.isFirstOnly = firstOnly;
    }

    public int getItemViewType(int position) {
        return this.mAdapter.getItemViewType(position);
    }

    public RecyclerView.Adapter<RecyclerView.ViewHolder> getWrappedAdapter() {
        return mAdapter;
    }
}
