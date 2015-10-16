package ift604.tp1.client;

import ift604.common.dispatch.Dispatcher;
import ift604.common.transport.StreamSenderReceiver;
import ift604.common.cargo.Cargo;
import ift604.common.transport.MarshallGeneral;
import ift604.common.transport.DatagramSenderReceiver;

/**
 * Created by taig1501 on 15-10-13.
 */
public class State {
    protected int boatsReceived;

    protected DatagramSenderReceiver udpSenderReceiver;
    protected Thread udpThread;
    protected MarshallGeneral<Cargo> udpMarshallGeneral;
    protected StreamSenderReceiver tcpSenderReceiver;
    protected Thread tcpThread;
    protected MarshallGeneral<Cargo> tcpMarshallGeneral;
    protected Dispatcher<Cargo> dispatcher;

    public int getBoatsReceived() {
        return boatsReceived;
    }

    public void setBoatsReceived(int boatsReceived) {
        this.boatsReceived = boatsReceived;
    }

    public DatagramSenderReceiver getUdpSenderReceiver() {
        return udpSenderReceiver;
    }

    public void setUdpSenderReceiver(DatagramSenderReceiver udpSenderReceiver) {
        this.udpSenderReceiver = udpSenderReceiver;
    }

    public Thread getUdpThread() {
        return udpThread;
    }

    public void setUdpThread(Thread udpThread) {
        this.udpThread = udpThread;
    }

    public MarshallGeneral<Cargo> getUdpMarshallGeneral() {
        return udpMarshallGeneral;
    }

    public void setUdpMarshallGeneral(MarshallGeneral<Cargo> udpMarshallGeneral) {
        this.udpMarshallGeneral = udpMarshallGeneral;
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
