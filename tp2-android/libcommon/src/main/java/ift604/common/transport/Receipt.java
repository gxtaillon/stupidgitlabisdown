package ift604.common.transport;

import java.io.Serializable;
import java.net.InetAddress;

import gxt.common.Challenge;
import gxt.common.Func0;
import gxt.common.Func1;

/**
 * Created by taig1501 on 15-10-13.
 */
public class Receipt <Ta extends Serializable> {
    protected Ta payload;
    protected InetAddress originAddress;
    protected int originPort;
    protected Func1<Ta, Challenge> reply;
    protected Func0<Challenge> close;

    public Receipt(Ta payload, InetAddress originAddress, int originPort, Func1<Ta, Challenge> reply, Func0<Challenge> close) {
        this.payload = payload;
        this.originAddress = originAddress;
        this.originPort = originPort;
        this.reply = reply;
        this.close = close;
    }

    public Ta getPayload() {
        return this.payload;
    }

    public InetAddress getOriginAddress() {
        return this.originAddress;
    }

    public int getOriginPort() {
        return this.originPort;
    }

    public Challenge reply(Ta a) {
        return this.reply.func(a);
    }

    public Challenge close() {
        return this.close.func();
    }
}
