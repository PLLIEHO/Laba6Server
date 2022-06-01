package server;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class AnswerSender {
    private SocketAddress address;

    public AnswerSender(SocketAddress address) {
        this.address = address;
    }

    public void send(DatagramChannel channel, ByteArrayOutputStream byteOutput) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(byteOutput.toByteArray());
        if (address != null) {
            channel.send(buffer, address);
            Server.LOG.info("Ответ клиенту {} отправлен.", address);
        }
    }
}
