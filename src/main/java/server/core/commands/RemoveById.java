package server.core.commands;


import server.DBConnection;
import server.Server;
import server.core.Collection;

public class RemoveById {
    public String removeById(String values, Collection collection, String user){
        String answer;
            long ID = Long.parseLong(values);
            collection.getCollection().stream().filter(human -> human.getId() == ID).forEach(human -> {
                new DBConnection().removeFromBD(human, user);
                Server.LOG.info("Объект с ID {} успешно удален.", ID);
                collection.removeHuman(human, user, collection);
            });
            return "Объект успешно удалён.";

    }
}
