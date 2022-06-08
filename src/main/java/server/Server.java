package server;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.core.Collection;
import sun.misc.Signal;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.CancellationException;

public class Server {
        public static int ServerPort;
        public final static Logger LOG = LoggerFactory.getLogger(Processor.class);
    public static void main(String[] args) {
            try {
                    LOG.info("Server set up and launched");
                    DatagramChannelBuilder builder = new DatagramChannelBuilder();
                    System.out.println("Введите порт...");
                    Scanner in = new Scanner(System.in);
                    try {
                            int port = Integer.parseInt(in.nextLine());
                            if(port<65535&&port>0) {
                                    ServerPort = port;
                            } else {
                                    LOG.info("Port {} is wrong", port);
                                    main(args);
                            }
                    } catch (NumberFormatException e){
                            LOG.info("Port is wrong. Relaunching server...");
                            main(args);
                    }
                    DBConnection connection = new DBConnection();
                    connection.getConnection();

                    InetSocketAddress address = new InetSocketAddress(ServerPort);
                    DatagramChannel channel = builder.build();
                    channel.bind(address);

                    Server.LOG.info("Datagram-channel opened. Port: {}", ServerPort);
                    channel.configureBlocking(false);

                    Processor processor = new Processor();
                    Collection collection = connection.load();
                    setupSignalHandler(channel);
                    processor.begin(channel, collection);
            } catch(IOException e){
                    Server.LOG.info("Server initialization mistake.");
                    e.printStackTrace();
                }
            }
        private static void setupSignalHandler(DatagramChannel channel) {
                Signal.handle(new Signal("INT"), signal -> {
                        try {
                                        Receiver.endFlag = true;
                                        DBConnection.connection.close();
                                        channel.close();
                                        System.exit(0);
                        } catch (IOException | SQLException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                        } catch (CancellationException | NullPointerException c){
                                System.exit(0);
                        }
                });
        }
}

