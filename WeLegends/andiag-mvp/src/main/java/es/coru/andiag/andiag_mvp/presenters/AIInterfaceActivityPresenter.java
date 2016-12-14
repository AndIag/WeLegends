package es.coru.andiag.andiag_mvp.presenters;

import android.content.Context;

import es.coru.andiag.andiag_mvp.views.AIInterfaceActivityView;

/**
 * Created by Canalejas on 14/12/2016.
 */

public interface AIInterfaceActivityPresenter<V extends Context & AIInterfaceActivityView> {

    V getView();

    void attach(V view);

    void detach();

}
