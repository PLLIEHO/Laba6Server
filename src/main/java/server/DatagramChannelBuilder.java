package server;

import java.io.IOException;
import java.nio.channels.DatagramChannel;


public class DatagramChannelBuilder {
    public DatagramChannel build() throws IOException {
        return DatagramChannel.open();
    }
}
