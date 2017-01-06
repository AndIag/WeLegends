package com.andiag.welegends.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andiag.commons.fragments.AIButterFragment;
import com.andiag.core.presenters.AIPresenter;
import com.andiag.statedlayout.StatedLayout;

/**
 * Created by andyq on 02/01/2017.
 */

public abstract class FragmentBase<P extends AIPresenter> extends AIButterFragment<P> {

    public StatedLayout statedLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        getStatedLayout(view);
        if (statedLayout == null)
            throw new IllegalStateException("StatedLayout not found in layout");
        return view;
    }

    private void getStatedLayout(ViewGroup viewGroup) {
        View v;
        if (viewGroup instanceof StatedLayout) {
            statedLayout = (StatedLayout) viewGroup;
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            v = viewGroup.getChildAt(i);
            if (v instanceof StatedLayout) {
                statedLayout = (StatedLayout) v;
                break;
            }
            if (v instanceof ViewGroup) {
                getStatedLayout((ViewGroup) v);
            }
        }
    }
}
