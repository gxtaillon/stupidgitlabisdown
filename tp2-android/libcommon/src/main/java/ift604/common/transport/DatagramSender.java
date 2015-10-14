package ift604.common.transport;

import java.io.Serializable;
import java.net.InetAddress;

import gxt.common.Challenge;
import gxt.common.Maybe;

/**
 * Created by taig1501 on 15-10-14.
 */
public interface DatagramSender {
    public <Ta extends Serializable> Challenge send(Ta a, InetAddress sendAddr, int sendPort);
}
