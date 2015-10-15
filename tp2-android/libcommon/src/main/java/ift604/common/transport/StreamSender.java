package ift604.common.transport;

import java.io.Serializable;

import gxt.common.Challenge;
import gxt.common.Maybe;

/**
 * Created by taig1501 on 15-10-14.
 */
public interface StreamSender {

    public <Ta extends Serializable> Challenge send(Ta a);
    public Maybe<StreamSender> connect();
}
