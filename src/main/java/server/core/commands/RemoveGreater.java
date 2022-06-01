package server.core.commands;


import server.Server;
import server.core.Collection;

public class RemoveGreater {
    private String value;
    private String tag;
    private Collection collection;
    public RemoveGreater(String value, String tag, Collection collection){
        this.value = value;
        this.collection = collection;
        this.tag = tag;
    }
    public String splitter(){
        switch(tag){
            case COORDSX:
                return searchmin(0);

            case COORDSY:
                return searchmin(1);

            case REALHERO:
                return searchmin(2);

            case HASTOOTHPICK:
                return searchmin(3);

            case IMPACTSPEED:
                return searchmin(4);

            case CARCOOL:
                return searchmin(5);

            default:
                Server.LOG.info("Поле не распознано.");
                return "Вы ввели неправильное поле. Попробуйте одно из этих: coordinates_x, coordinates_y,\n" +
                        "realhero, hastoothpick, impactspeed, carcool.";
        }
    }
    private String searchmin(int flag) {
        switch (flag){
            case 0:
                try {
                    float x = Float.parseFloat(value);
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getCoordinatesX() > x);
                    break;
                } catch (NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
            case 1:
                try {
                    double y = Double.parseDouble(value);
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getCoordinatesY() > y);
                    break;
                } catch (NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
            case 2:
                if(value.equalsIgnoreCase("true")){
                    break;
                } else if(value.equalsIgnoreCase("false")){
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getRealHero());
                    break;
                } else{Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";}
            case 3:
                if(value.equalsIgnoreCase("true")){
                    break;
                } else if(value.equalsIgnoreCase("false")){
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getHasToothPick());
                    break;
                } else if(value.equals("")){
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getHasToothPick()!=null);
                    break;
                } else{Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";}
            case 4:
                try {
                    if(!value.equals("")) {
                        long speed = Long.parseLong(value);
                        collection.getCollection().removeIf(humanBeing -> humanBeing.getImpactSpeed() > speed);
                    } else{
                        collection.getCollection().removeIf(humanBeing -> humanBeing.getImpactSpeed() != null);
                    }
                    break;
                } catch(NumberFormatException e){
                    Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";
                }
            case 5:
                if(value.equalsIgnoreCase("true")){
                    break;
                } else if(value.equalsIgnoreCase("false")){
                    collection.getCollection().removeIf(humanBeing -> humanBeing.getCarCool());
                    break;
                } else{Server.LOG.info("Аргумент не распознан."); return "Аргумент не распознан.";}
        }
        return null;
    }
    private static final String REALHERO = "realhero";
    private static final String HASTOOTHPICK = "hastoothpick";
    private static final String IMPACTSPEED = "impactspeed";
    private static final String COORDSX = "coordinates_x";
    private static final String COORDSY = "coordinates_y";
    private static final String CARCOOL = "carcool";
}