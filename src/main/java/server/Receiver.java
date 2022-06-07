package server;

import common.Answer;
import common.Request;
import common.Serializer;
import server.core.Collection;
import server.core.commands.History;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class Receiver extends RecursiveAction {
    private SocketAddress address;
    private DatagramChannel channel;
    private ByteBuffer clientRequest;
    private Collection collection;
    private Queue<InetSocketAddress> confirmation;
    private Processor processor;
    private ForkJoinPool fjp;
    public static boolean endFlag = false;

    public Receiver(SocketAddress address, DatagramChannel channel, ByteBuffer clientRequest, Collection collection, Queue<InetSocketAddress> confirmation, Processor processor, ForkJoinPool fjp){
        this.address = address;
        this.channel = channel;
        this.clientRequest = clientRequest;
        this.collection = collection;
        this.confirmation = confirmation;
        this.processor = processor;
        this.fjp = fjp;
    }
    History history = new History();
    @Override
    protected void compute() {
            if (address != null) {
                Serializer serializer = new Serializer();
                InetSocketAddress clientAddress = (InetSocketAddress) address;
                clientRequest.flip();
                Server.LOG.info("Packet received from client {}", address.toString());

                Request request = (Request) serializer.deserialize(clientRequest.array());
                if (!processor.checkApproval(request)) {
                    confirmation.add(clientAddress);
                    ForkJoinPool executePool = new ForkJoinPool();
                    ForkJoinPool sendPool = new ForkJoinPool(2);
                    CommandExecutor executor = new CommandExecutor(request, collection, channel, history);
                    try {
                        AnswerSender sender = new AnswerSender(address, channel, serializer.serialize(new Answer(executePool.invoke(executor))));
                        sendPool.invoke(sender);
                        sendPool.shutdown();
                        executePool.shutdown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    confirmation.remove();
                }
                clientRequest.clear();
                fjp.shutdown();
            }
        }
    }


