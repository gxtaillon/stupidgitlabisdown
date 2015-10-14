package ift604.common.cargo;

import java.io.Serializable;
import java.util.ArrayList;
import ift604.common.models.*;

public class MatchStats implements Serializable {
	
	private static final long serialVersionUID = 4089657874246736528L;
    private static Match match;

    public Match getMatch(){
        return match;
    }

}
