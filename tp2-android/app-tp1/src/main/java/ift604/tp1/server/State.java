package ift604.tp1.server;

import ift604.common.models.ListeDesMatchs;
import ift604.common.models.ParisManager;

/**
 * Created by taig1501 on 15-10-15.
 */
public class State {
    protected ListeDesMatchs listeDesMatchs;
    protected ParisManager parisManager;

    public ParisManager getParisManager() {
        return parisManager;
    }

    public void setParisManager(ParisManager parisManager) {
        this.parisManager = parisManager;
    }

    public ListeDesMatchs getListeDesMatchs() {
        return listeDesMatchs;
    }

    public void setListeDesMatchs(ListeDesMatchs listeDesMatchs) {
        this.listeDesMatchs = listeDesMatchs;
    }
}
