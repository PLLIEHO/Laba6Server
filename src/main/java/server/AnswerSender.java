package server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.RecursiveAction;

public class AnswerSender extends RecursiveAction {
    private SocketAddress address;
    private DatagramChannel channel;
    private ByteArrayOutputStream byteOutput;


    public AnswerSender(SocketAddress address, DatagramChannel channel, ByteArrayOutputStream byteOutput) {
        this.address = address;
        this.channel = channel;
        this.byteOutput = byteOutput;
    }

    @Override
    protected void compute() {
        ByteBuffer buffer = ByteBuffer.wrap(byteOutput.toByteArray());
        if (address != null) {
            try {
                channel.send(buffer, address);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.LOG.info("Answer for client {} on the way.", address);
        }
    }
}
