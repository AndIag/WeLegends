package andiag.coru.es.welegends.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Andy on 27/06/2015.
 */
public class ScaleInAnimationAdapter extends AnimationAdapter {
    private static final float DEFAULT_SCALE_FROM = 0.5F;
    private final float mFrom;

    public ScaleInAnimationAdapter(RecyclerView.Adapter adapter) {
        this(adapter, 0.5F);
    }

    public ScaleInAnimationAdapter(RecyclerView.Adapter adapter, float from) {
        super(adapter);
        this.mFrom = from;
    }

    protected Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", new float[]{this.mFrom, 1.0F});
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", new float[]{this.mFrom, 1.0F});
        return new ObjectAnimator[]{scaleX, scaleY};
    }
}

