package ift604.common.transport;

import java.io.Serializable;
import java.net.InetAddress;

import gxt.common.Challenge;
import gxt.common.Func1;

/**
 * Created by taig1501 on 15-10-13.
 */
public class Receipt <Ta extends Serializable> {
    protected Ta payload;
    protected InetAddress originAddress;
    protected int originPort;
    protected Func1<Ta, Challenge> reply;

    public Receipt(Ta a, InetAddress oa, int op, Func1<Ta, Challenge> reply) {
        this.payload = a;
        this.originAddress = oa;
        this.originPort = op;
        this.reply = reply;
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
}
