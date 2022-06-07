package server.core.commands;


import server.CommandExecutor;
import server.DBConnection;
import server.core.Collection;
import server.core.comparators.CoordsXComparator;
import server.core.comparators.CoordsYComparator;
import server.core.comparators.ImpactSpeedComparator;
import server.data.HumanBeing;
import server.data.Mood;
import server.data.WeaponType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class AddCore extends RecursiveTask<String> {
    private boolean addIfMaxFlag = false;
    private String addIfMaxElement;
    private CommandExecutor core;
    private List<HumanBeing> maxList = new ArrayList<>();
    private Collection collection;
    private String[] args;
    private String answer;
    private boolean deleteFlag = false;
    private HumanBeing human = new HumanBeing();
    private String user;

    public AddCore(Collection collection, CommandExecutor core, String[] args, String user){
        this.collection = collection;
        this.core = core;
        this.args = args;
        this.user = user;
    }

    @Override
    public String compute() {
        System.out.println(args);
        maxList.clear();
        maxList.addAll(collection.getCollection());
        if (collection.getCollection().size() > 0) {
            human.setId(collection.getHuman().getId() + 1);
        } else {
            human.setId(1);
        }
        addName();
        try {
            addCoordsX();
            addCoordsY();
            Date date = new Date();
            human.setCreationDate(date);
            isRealHero();
        hasToothPick();
        impactSpeed();
        weaponType();
        mood();
        addCarName();
        setCarCool();
        human.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addIfMaxFlag = false;
        if(!deleteFlag) {
            DBConnection connection = new DBConnection();
            if(connection.addToBD(human, user)){
                answer = collection.addHuman(collection.createHumanToAdd(human));
            } else {
                answer = "Could not add the object. Try again.";
            }
        } else {
            answer = "Could not add the object. Try again.";
            deleteFlag = false;
        }
        return answer;

    }

    public void addName() {
        String name = args[0];
        human.setName(name);
    }

    public void addCarName() {
        String name = args[8];
        human.setCarName(name);
    }

    public void setCarCool() throws IOException {
        String cool = args[9];
            if (cool.equals("true")) {
                human.setCarCool(true);
            } else if (cool.equals("false")) {
                human.setCarCool(false);
            }
    }

    public void mood() {
        String moodType = args[7].toLowerCase();
        Mood mood = Mood.parseMood(moodType);
        human.setMood(mood);
        }


    public void weaponType() {
        String weapon = args[6].toLowerCase();
        human.setWeaponType(WeaponType.parseWeaponType(weapon));
    }

    public void isRealHero() throws IOException {
        String hero = args[3];
            if (hero.equals("true")) {
                human.setRealHero(true);
            } else if (hero.equals("false")) {
                human.setRealHero(false);
            }
    }

    public void addCoordsX() throws IOException {
        String x = args[1];
        if (!addIfMaxFlag || (maxCheck(COORDSX, x)&&addIfMaxElement.equals(COORDSX))) {
            float floatX = Float.parseFloat(x);
            human.setCoordinatesX(floatX);
        }
    }

    public void addCoordsY() throws IOException {
        String y = args[2];
        if (!addIfMaxFlag || (maxCheck(COORDSY, y)&&addIfMaxElement.equals(COORDSY))) {
            human.setCoordinatesY(Double.parseDouble(y));
        }
    }

    public void hasToothPick() throws IOException {
        String toothPick = args[4];
            switch (toothPick) {
                case "true":
                    human.setHasToothPick(true);
                    break;
                case "false":
                    human.setHasToothPick(false);
                    break;
                case "null":
                    human.setHasToothPick(null);
                    break;
        }
    }

    public void impactSpeed() throws IOException {
        String speed = args[5];
        if (!speed.equals("null")) {
                if (!addIfMaxFlag || (maxCheck(IMPACTSPEED, speed)&&addIfMaxElement.equals(IMPACTSPEED))) {
                    long longSpeed = Long.parseLong(speed);
                    human.setImpactSpeed(longSpeed);
                }
        } else {
            human.setImpactSpeed(null);
        }
    }

    public String add(String addIfMaxElement, boolean addIfMaxFlag) throws IOException {
        this.setAddIfMaxElement(addIfMaxElement);
        this.addIfMaxFlag = addIfMaxFlag;
        this.fork();
        return this.join();
    }

    public boolean maxCheck(String element, String value){
        float coordsMinX = -316;
        double coordsMinY = -100000000;
        long impactSpeed = -100000000L;
        if (maxList.size() > 0) {
            for (HumanBeing humanBeing : maxList) {
                switch (addIfMaxElement) {
                    case COORDSX:
                        maxList = maxList.stream().sorted(new CoordsXComparator()).collect(Collectors.toList());
                        Collections.reverse(maxList);
                        coordsMinX = maxList.get(0).getCoordinatesX();
                        break;
                    case COORDSY:
                        maxList = maxList.stream().sorted(new CoordsYComparator()).collect(Collectors.toList());
                        Collections.reverse(maxList);
                        coordsMinY = maxList.get(0).getCoordinatesY();
                        break;
                    case IMPACTSPEED:
                        maxList = maxList.stream().sorted(new ImpactSpeedComparator()).collect(Collectors.toList());
                        Collections.reverse(maxList);
                        impactSpeed = maxList.get(0).getImpactSpeed();
                        break;
                    default:
                        System.out.println("Вы ввели неправильный элемент.");

                }
            }
        }
        switch (element) {
            case COORDSX:
                if (coordsMinX >= Float.parseFloat(value)) {
                    this.addIfMaxFailure();
                } else {
                    addIfMaxFlag = false;
                    return (true);
                }
                break;
            case COORDSY:
                if (coordsMinY >= Double.parseDouble(value)) {
                    this.addIfMaxFailure();
                } else {
                    addIfMaxFlag = false;
                    return (true);
                }
                break;
            case IMPACTSPEED:
                if (impactSpeed > Long.parseLong(element)) {
                    this.addIfMaxFailure();
                } else {
                    addIfMaxFlag = false;
                    return (true);
                }
                break;
            default:
                addIfMaxFlag = false;
                return (true);
        }
        return (true);
    }

    public void addIfMaxFailure(){
        deleteFlag = true;
        addIfMaxFlag = false;
        answer = "Данные, введенные ранее, не превышают значения максимального элемента. Новый элемент не будет добавлен.";

    }

    public void setAddIfMaxFlag(boolean flag) {
        addIfMaxFlag = flag;
    }

    public void setAddIfMaxElement(String addIfMaxElement) {
        this.addIfMaxElement = addIfMaxElement;
    }


    private static final String NAME = "name";
    private static final String COORDS = "coordinates";
    private static final String REALHERO = "realhero";
    private static final String HASTOOTHPICK = "hastoothpick";
    private static final String IMPACTSPEED = "impactspeed";
    private static final String WEAPONTYPE = "weapontype";
    private static final String MOOD = "mood";
    private static final String CAR = "car";
    private static final String COORDSX = "coordinates_x";
    private static final String COORDSY = "coordinates_y";
    private static final String CARCOOL = "carcool";


}

