package andiag.coru.es.welegends.fragments;

import andiag.coru.es.welegends.activities.NotifiableActivity;

/**
 * Created by canalejas on 17/06/16.
 */
public interface NotifiableFragment<T> {

    //TODO change ActivitySummonerData to ActivitiNotifiable

    void setNotifiableActivity(NotifiableActivity<T> notifiableActivity);

}
