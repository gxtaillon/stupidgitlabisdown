package ift604.tp1.client;

import ift604.common.dispatch.Dispatcher;
import ift604.common.tcp.StreamSenderReceiver;
import ift604.common.transport.Cargo;
import ift604.common.transport.MarshallGeneral;

/**
 * Created by taig1501 on 15-10-13.
 */
public class State {
    public int boatsReceived;

    StreamSenderReceiver tcpSenderReceiver;
    Thread tcpThread;
    MarshallGeneral<Cargo> tcpMarshallGeneral;
    Dispatcher<Cargo> dispatcher;

    public int getBoatsReceived() {
        return boatsReceived;
    }

    public void setBoatsReceived(int boatsReceived) {
        this.boatsReceived = boatsReceived;
    }

    public StreamSenderReceiver getTcpSenderReceiver() {
        return tcpSenderReceiver;
    }

    public void setTcpSenderReceiver(StreamSenderReceiver tcpSenderReceiver) {
        this.tcpSenderReceiver = tcpSenderReceiver;
    }

    public Thread getTcpThread() {
        return tcpThread;
    }

    public void setTcpThread(Thread tcpThread) {
        this.tcpThread = tcpThread;
    }

    public MarshallGeneral<Cargo> getTcpMarshallGeneral() {
        return tcpMarshallGeneral;
    }

    public void setTcpMarshallGeneral(MarshallGeneral<Cargo> tcpMarshallGeneral) {
        this.tcpMarshallGeneral = tcpMarshallGeneral;
    }

    public Dispatcher<Cargo> getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(Dispatcher<Cargo> dispatcher) {
        this.dispatcher = dispatcher;
    }
}
