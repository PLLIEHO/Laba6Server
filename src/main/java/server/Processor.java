package server;


import common.Request;
import common.Serializer;
import server.core.Collection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;

public class Processor  {
    public SocketAddress address;
    private Queue<InetSocketAddress> confirmation = new LinkedList<>();

    public void begin(DatagramChannel channel, Collection collection) {
        try {
                ByteBuffer clientRequest = ByteBuffer.allocate(Serializer.SIZE);
                Selector selector = Selector.open();
                SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

                getPacket(channel, clientRequest, selector, key, collection);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public void getPacket(DatagramChannel channel, ByteBuffer clientRequest, Selector selector, SelectionKey key, Collection collection) throws IOException {
        while (true) {
            int readyChannels = selector.selectNow();
            if (readyChannels == 0) continue;
            else {
                break;
            }
        }
        while (true) {
            if(channel.isOpen()) {
                try {
                    ForkJoinPool fjp = new ForkJoinPool(2);
                    address = channel.receive(clientRequest);
                    if (address != null) {
                        try {
                            fjp.invoke(new Receiver(address, channel, clientRequest, collection, confirmation, this, fjp));
                        } catch (CancellationException e) {
                            fjp.shutdownNow();
                        }
                    } else {
                        clientRequest.clear();
                    }
                } catch (ClosedChannelException e){
                }
            }
        }
    }

    public boolean checkApproval(Request request) throws NullPointerException{
            if (confirmation.size() > 0 ) {
                if (request.getArgument().getArgB() != null && Objects.equals(request.getArgument().getArgB(), confirmation.peek().getAddress().getHostAddress())) {
                    Server.LOG.info("Client {} approved answer claiming.", request.getArgument().getArgB());
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
    }
}

