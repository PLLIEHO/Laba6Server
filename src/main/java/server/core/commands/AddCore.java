package server.core.commands;


import server.CommandExecutor;
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
import java.util.stream.Collectors;

public class AddCore {
    private boolean addIfMaxFlag = false;
    private String addIfMaxElement;
    private CommandExecutor core;
    private List<HumanBeing> maxList = new ArrayList<>();
    private Collection collection;
    private String[] args;
    private String answer;
    private boolean deleteFlag = false;
    HumanBeing human = new HumanBeing();

    public AddCore(Collection collection, CommandExecutor core, String[] args){
        this.collection = collection;
        this.core = core;
        this.args = args;
    }

    public String add() throws IOException {
        maxList.clear();
        maxList.addAll(collection.getCollection());
        if (collection.getCollection().size() > 0) {
            human.setId(collection.getHuman().getId() + 1);
        } else {
            human.setId(1);
        }
        addName();
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
        addIfMaxFlag = false;
        if(!deleteFlag) {
            collection.getCollection().add(human);
            answer = "Добавление прошло успешно.";
        } else {deleteFlag = false;
        }
        return answer;

    }

    public void addName() {
        String name = args[0];
        if (!name.equals("")) {
            human.setName(name);
        } else {
            answer = "Имя не должно быть пустым.";
            deleteFlag = true;
        }
    }

    public void addCarName() {
        String name = args[8];
        if (!name.equals("")) {
            human.setCarName(name);
        } else {
            answer = "Имя машины не должно быть пустым.";
            deleteFlag = true;
        }
    }

    public void setCarCool() throws IOException {
        String cool = args[9].toUpperCase();
        if (!addIfMaxFlag || maxCheck(REALHERO, cool)) {
            if (cool.equals("ДА")) {
                human.setCarCool(true);
            } else if (cool.equals("НЕТ")) {
                human.setCarCool(false);
            } else {
                answer = "Данные по полю carCool не верны.";
                deleteFlag = true;
            }
        }
    }

    public void mood() {
        String moodType = args[7].toLowerCase();
        switch (moodType) {
            case "sadness":
                human.setMood(Mood.SADNESS);
                break;
            case "gloom":
                human.setMood(Mood.GLOOM);
                break;
            case "apathy":
                human.setMood(Mood.APATHY);
                break;
            case "calm":
                human.setMood(Mood.CALM);
                break;
            case "rage":
                human.setMood(Mood.RAGE);
                break;
            default:
                answer = "Данные по полю mood не распознаны.";
                deleteFlag = true;

        }
    }

    public void weaponType() {
        String weapon = args[6].toLowerCase();
        switch (weapon) {
            case "axe":
                human.setWeaponType(WeaponType.AXE);
                break;
            case "pistol":
                human.setWeaponType(WeaponType.PISTOL);
                break;
            case "shotgun":
                human.setWeaponType(WeaponType.SHOTGUN);
                break;
            case "rifle":
                human.setWeaponType(WeaponType.RIFLE);
                break;
            default:
                answer = "Данные по полю weaponType не распознаны.";
                deleteFlag = true;

        }
    }

    public void isRealHero() throws IOException {
        String hero = args[3].toUpperCase();
        if (!addIfMaxFlag || maxCheck(REALHERO, hero)) {
            if (hero.equals("ДА")) {
                human.setRealHero(true);
            } else if (hero.equals("НЕТ")) {
                human.setRealHero(false);
            } else {
                answer = "Данные по полю isRealHero не верны.";
                deleteFlag = true;
            }
        }
    }

    public void addCoordsX() {
        String x = args[1];
        try {
            float floatX = Float.parseFloat(x);
            if (!addIfMaxFlag || maxCheck(COORDSX, x)) {
                if (floatX > -316) {
                    human.setCoordinatesX(floatX);
                } else {
                    answer = "Данные по полю coordsX не верны.";
                    deleteFlag = true;
                }
            }
        } catch (NumberFormatException | IOException e) {
            answer = "Данные по полю coordsX не верны.";
            deleteFlag = true;
        }
    }

    public void addCoordsY() {
        String y = args[2];
        try {
            if (!addIfMaxFlag || maxCheck(COORDSY, y)) {
                human.setCoordinatesY(Double.parseDouble(y));
            }
        } catch (NumberFormatException | IOException e) {
            answer = "Данные по полю coordsY не верны.";
            deleteFlag = true;
        }
    }

    public void hasToothPick() throws IOException {
        String toothPick = args[4].toUpperCase();
        if (!addIfMaxFlag || maxCheck(HASTOOTHPICK, toothPick)) {
            switch (toothPick) {
                case "ДА":
                    human.setHasToothPick(true);
                    break;
                case "НЕТ":
                    human.setHasToothPick(false);
                    break;
                case "":
                    human.setHasToothPick(null);
                    break;
                default:
                    answer = "Данные по полю hasToothPick не верны.";
                    deleteFlag = true;
            }
        }
    }

    public void impactSpeed() {
        String speed = args[5];
        if (!speed.equals("")) {
            try {
                if (!addIfMaxFlag || maxCheck(IMPACTSPEED, speed)) {
                    long longSpeed = Long.parseLong(speed);
                    if (longSpeed > -902) {
                        human.setImpactSpeed(longSpeed);
                    } else {
                        answer = "Данные по полю impactSpeed не верны.";
                        deleteFlag = true;
                    }
                }
            } catch (NumberFormatException | IOException e) {
                answer = "Данные по полю impactSpeed не верны.";
                deleteFlag = true;
            }
        } else {
            human.setImpactSpeed(null);
        }
    }

    public String add(String addIfMaxElement) throws IOException {
        this.setAddIfMaxElement(addIfMaxElement);
        this.addIfMaxFlag = true;
        return this.add();
    }

    public boolean maxCheck(String element, String value) throws IOException {
        float coordsMinX = -316;
        double coordsMinY = -100000000;
        boolean realHero = false;
        Boolean hasToothpick = false;
        long impactSpeed = -100000000L;
        boolean carCool = false;
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
            case REALHERO:
                if (!realHero && value.equals("false")) {
                    this.addIfMaxFailure();
                } else if (realHero) {
                    this.addIfMaxFailure();
                } else {
                    addIfMaxFlag = false;
                    return (true);
                }
                break;
            case HASTOOTHPICK:
                if (!hasToothpick && value.equals("false")) {
                    this.addIfMaxFailure();
                } else if (hasToothpick) {
                    this.addIfMaxFailure();
                } else if (value.equals("")) {
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
            case CARCOOL:
                if (!carCool && value.equals("false")) {
                    this.addIfMaxFailure();
                } else if (carCool) {
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

    public void addIfMaxFailure() throws IOException {
        System.out.println("Данные, введенные ранее, не превышают значения максимального элемента. Новый элемент не будет добавлен.");
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

