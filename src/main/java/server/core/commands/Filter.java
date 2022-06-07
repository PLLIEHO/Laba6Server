package server.core.commands;


import server.core.Collection;

public class Filter {
    String answer;
    public String filter(Collection collection, String value){
            collection.getCollection().stream().filter(humanBeing -> humanBeing.getName().contains(value)).forEachOrdered(humanBeing -> answer = answer + "\n" + humanBeing.toString());
            return null;
    }
}
