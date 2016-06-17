package andiag.coru.es.welegends.activities;

/**
 * Created by canalejas on 17/06/16.
 */
public interface NotifiableActivity<T extends Object> {

    void notifyDataChange(T o);

}
