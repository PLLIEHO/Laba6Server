package server.core;


import server.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class XMLSearcher {
    public Collection searchFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Collection collection = new Collection();
        try {
            if(file.exists()&&file.canRead()) {
                Parser parser = new Parser(collection);
                parser.start(filename);
                Server.LOG.info("Файл успешно прочитан. Объекты загружены в коллекцию");

            } else {Server.LOG.info("Данного файла {} не существует. Сервер будет выключен.", filename); System.exit(1);}
        } catch (FileNotFoundException e) {
            if(file.exists()){
                Server.LOG.info("Файл {} заблокирован для чтения. Сервер будет выключен.", filename);
                System.exit(1);}
            } catch (IOException e) {
            e.printStackTrace();
        }
        return collection;
    }
}
