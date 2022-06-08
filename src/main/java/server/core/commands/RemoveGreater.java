package server.core.commands;


import server.DBConnection;
import server.Server;
import server.core.Collection;
import server.data.HumanBeing;

public class RemoveGreater {
    private String value;
    private String tag;
    private Collection collection;
    public RemoveGreater(String value, String tag, Collection collection){
        this.value = value;
        this.collection = collection;
        this.tag = tag;
    }
    public String splitter(String user){
        switch(tag){
            case COORDSX:
                return searchmin(0, user);

            case COORDSY:
                return searchmin(1, user);

            case IMPACTSPEED:
                return searchmin(4, user);

            default:
                Server.LOG.info("Поле не распознано.");
                return "Вы ввели неправильное поле. Попробуйте одно из этих: coordinates_x, coordinates_y,\n" +
                        "impactspeed.";
        }
    }
    private String searchmin(int flag, String user) {
        switch (flag){
            case 0:
                try {
                    float x = Float.parseFloat(value);
                    collection.getCollection().stream().filter(human -> human.getCoordinatesX() > x).forEach(human -> {
                        new DBConnection().removeFromBD(human, user);
                        collection.removeHuman(human, user, collection);
                    });
                    return "Объекты успешно удалены";
                } catch (NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
            case 1:
                try {
                    double y = Double.parseDouble(value);
                    collection.getCollection().stream().filter(human -> human.getCoordinatesY() > y).forEach(human -> {
                        new DBConnection().removeFromBD(human, user);
                        collection.removeHuman(human, user, collection);
                    });
                    return "Объекты успешно удалены";
                } catch (NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
            case 4:
                try {
                    if(!value.equals("")) {
                        long speed = Long.parseLong(value);
                        collection.getCollection().stream().filter(human -> human.getImpactSpeed() > speed).forEach(human -> {
                            new DBConnection().removeFromBD(human, user);
                            collection.removeHuman(human, user, collection);
                        });
                    } else{
                        collection.getCollection().stream().filter(human -> human.getImpactSpeed()!=null).forEach(human -> {
                            new DBConnection().removeFromBD(human, user);
                            collection.removeHuman(human, user, collection);
                        });
                    }
                    return "Объекты успешно удалены";
                } catch(NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
        }
        return null;
    }
    private static final String IMPACTSPEED = "impactspeed";
    private static final String COORDSX = "coordinates_x";
    private static final String COORDSY = "coordinates_y";
}