package server.core.commands;


import server.core.Collection;
import server.data.HumanBeing;

public class MaxByRealHero {
   public String maxByRealHero(Collection collection) {
       if (collection.getCollection().size() > 0) {
           return collection.getCollection().stream().filter(HumanBeing::getRealHero).findFirst().map(HumanBeing::toString).orElse("В коллекции нет нужных объектов.");
       }
       return "В коллекции нет объектов.";
   }
}
