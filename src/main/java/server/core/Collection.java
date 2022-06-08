package server.core;


import server.DBConnection;
import server.data.HumanBeing;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Класс отвечает за управление и хранение коллекции
 */
public class Collection  {
    ConcurrentLinkedDeque<HumanBeing> humanQue = new ConcurrentLinkedDeque<>();
    Date data = new Date();

    /**
     *
     * @param human новый объект класса HumanBeing
     */
    public String addHuman(HumanBeing human, Collection collection){
            collection.getCollection().addLast(human);
            return "Added successfully.";
    }

    public String removeHuman(HumanBeing humanBeing, String user, Collection collection){
            if(user.equals(humanBeing.getUser())) {
                collection.getCollection().remove(humanBeing);

                return "Removed successfully.";
            } else {
                return "You do not own this object.";
            }
    }

    public void clearColl(String user, Collection collection){
        collection.getCollection().removeIf(human -> human.getUser().equals(user));
    }

    public HumanBeing createHumanToAdd(HumanBeing human){
        DBConnection connection = new DBConnection();
        long ID = connection.getID();
        HumanBeing humanToAdd = new HumanBeing();
        humanToAdd.setId(ID);
        humanToAdd.setName(human.getName());
        humanToAdd.setCreationDate(human.getCreationDate());
        humanToAdd.setCoordinatesX(human.getCoordinatesX());
        humanToAdd.setCoordinatesY(human.getCoordinatesY());
        humanToAdd.setRealHero(human.getRealHero());
        humanToAdd.setHasToothPick(human.getHasToothPick());
        humanToAdd.setWeaponType(human.getWeaponType());
        humanToAdd.setMood(human.getMood());
        humanToAdd.setImpactSpeed(human.getImpactSpeed());
        humanToAdd.setCarName(human.getCarName());
        humanToAdd.setCarCool(human.getCarCool());
        return humanToAdd;
    }
    /**
     *
     * @return возвращает последний в очереди объект коллекции
     */
    public HumanBeing getHuman(){
        return humanQue.getLast();
    }

    public ConcurrentLinkedDeque<HumanBeing> getCollection(){
        return humanQue;
    }
    /**
     *
     * @return возвращает дату инициализации коллекции
     */
    public Date getData(){return data;}


}
