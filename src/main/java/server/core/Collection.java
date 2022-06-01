package server.core;


import server.data.HumanBeing;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;

/**
 * Класс отвечает за управление и хранение коллекции
 */
public class Collection {
    Deque<HumanBeing> humanQue = new ArrayDeque<>();
    Date data = new Date();

    /**
     *
     * @param human новый объект класса HumanBeing
     */
    public void addHuman(HumanBeing human){
        humanQue.add(human);
    }

    /**
     *
     * @return возвращает последний в очереди объект коллекции
     */
    public HumanBeing getHuman(){
        return humanQue.getLast();
    }

    public Deque<HumanBeing> getCollection(){
        return humanQue;
    }
    /**
     *
     * @return возвращает дату инициализации коллекции
     */
    public Date getData(){return data;}

    /**
     * Ищет все объекты коллекции, поле Name которых содержит данную подстроку
     * @param value Подстрока, которая должна содержаться в имени
     */
    public void searchName(String value){
        for(HumanBeing humanBeing : humanQue){
            if(humanBeing.getName().contains(value)){
                System.out.println(humanBeing);
            }
        }
    }

}
