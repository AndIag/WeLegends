package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;
import android.support.v4.app.Fragment;

import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView;
import es.coru.andiag.andiag_mvp.views.AIInterfaceFragmentView;

/**
 * Created by Canalejas on 14/12/2016.
 */

public interface AIInterfaceFragmentPresenter<V extends Fragment & AIInterfaceFragmentView, PV extends Context & AIInterfaceActivityView> {

    V getView();

    PV getParent();

    void attach(V view, PV parentView);

    void detach();

}
