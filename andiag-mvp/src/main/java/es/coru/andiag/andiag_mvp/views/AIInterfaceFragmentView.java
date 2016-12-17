package es.coru.andiag.andiag_mvp.views;

/**
 * Created by Canalejas on 11/12/2016.
 */

public interface AIInterfaceFragmentView<P> {

    void setPresenter(P presenter);

    P getPresenter();

}
