package server;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.core.Collection;
import server.core.Saver;
import server.core.XMLSearcher;
import sun.misc.Signal;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class Server {
        public static int ServerPort;
        public static String filepath;
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
                    InetSocketAddress address = new InetSocketAddress(ServerPort);
                    DatagramChannel channel = builder.build();
                    channel.bind(address);

                    Server.LOG.info("Datagram-channel opened. Port: {}", ServerPort);
                    channel.configureBlocking(false);

                    XMLSearcher searchXML = new XMLSearcher();
                    Processor processor = new Processor(args[0]);
                    Collection collection = searchXML.searchFile(args[0]);
                    //setupSignalHandler(collection, args[0]);
                    filepath = args[0];
                    processor.begin(channel, collection);
            } catch(IOException e){
                    Server.LOG.info("Server initialization mistake.");
                }
            }
        /*private static void setupSignalHandler(Collection collection, String filepath) {
                Signal.handle(new Signal("INT"), signal -> {
                        try {
                                LOG.info("Сохранение...");
                                Saver outputCore = new Saver();
                                outputCore.save(filepath, collection);
                                LOG.info("Сохранение прошло успешно.");
                        } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                        }
                });
        }*/
        }

