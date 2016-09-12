package andiag.coru.es.welegends.fragments;

import andiag.coru.es.welegends.activities.ActivityNotifiable;

/**
 * Created by canalejas on 17/06/16.
 */
public interface FragmentNotifiable<T> {

    void setActivityNotifiable(ActivityNotifiable<T> activityNotifiable);

    void setProgressBarState(int viewState);

}
