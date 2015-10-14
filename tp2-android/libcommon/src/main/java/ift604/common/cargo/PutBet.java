package ift604.common.cargo;

import java.io.Serializable;

/**
 * Created by taig1501 on 15-10-13.
 */
public class PutBet implements Serializable {
    private Integer matchId;
    private Integer userId;
    private Double amount;
    private static final long serialVersionUID = 4089657000226736898L;

    public PutBet(Integer matchId, Integer userId, Double amount) {
        this.matchId = matchId;
        this.userId = userId;
        this.amount = amount;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public String toString() {
        return "PutBet";
    }
}
