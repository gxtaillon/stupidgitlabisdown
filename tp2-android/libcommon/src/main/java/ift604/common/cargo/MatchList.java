package ift604.common.cargo;

import java.io.Serializable;

import ift604.common.models.ListeDesMatchs;

/**
 * Created by Phil on 2015-10-14.
 */
public class MatchList implements Serializable {

    private static final long serialVersionUID = 4089657874246736528L;
    private static ListeDesMatchs ListeDeMatchs;

    public ListeDesMatchs getMatchList(){
        return ListeDeMatchs;
    }

}
