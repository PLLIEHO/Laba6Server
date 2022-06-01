package server.core.commands;


import server.Server;
import server.core.Collection;

public class RemoveById {
    public String removeById(String values, Collection collection){
        try {
            long ID = Long.parseLong(values);
            collection.getCollection().stream().filter(human -> human.getId() == ID).forEach(human -> {
                collection.getCollection().remove(human);
                Server.LOG.info("Объект с ID {} успешно удален.", ID);
            });
            return "Объект успешно удалён.";

        } catch (NumberFormatException e) {
            return "ID введён неверно. Пожалуйста, повторите ввод.";
        }
    }
}
