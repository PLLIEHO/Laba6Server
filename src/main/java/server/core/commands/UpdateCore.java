package server.core.commands;



import common.ElementList;
import server.core.Collection;
import server.data.HumanBeing;
import server.data.Mood;
import server.data.WeaponType;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateCore {
    private Long ID;
    private List<String> element;
    private Collection collection;

    public UpdateCore(Collection collection, String ID, List<String> element){
        this.ID = Long.parseLong(ID);
        this.element = element;
        this.collection = collection;
    }

    public String update(){
        String answer;
        String tag = element.get(0);
        String arg = element.get(1);
        List<HumanBeing> human = collection.getCollection().stream().filter(s -> s.getId()==ID).collect(Collectors.toList());
            if (human.size()>0) {
                HumanBeing humanBeing = human.get(0);
                switch (tag) {
                    case ElementList.NAME:
                        String name = arg;
                        if (!name.equals("")) {
                            humanBeing.setName(name);
                            answer = "Обновление прошло успешно.";
                        } else {
                            answer = "Имя не должно быть пустым.";
                        }
                        return answer;
                    case ElementList.COORDSX:
                        String x = arg;
                        try {
                            float floatX = Float.parseFloat(x);
                                if (floatX > -316) {
                                    humanBeing.setCoordinatesX(floatX);
                                    answer = "Обновление прошло успешно.";
                                } else {
                                    answer = "Данные по полю coordsX не верны.";

                                }
                        } catch (NumberFormatException e) {
                            answer = "Данные по полю coordsX не верны.";
                        }
                        return answer;
                    case ElementList.COORDSY:
                        String y = arg;
                        try {
                            humanBeing.setCoordinatesY(Double.parseDouble(y));
                            answer = "Обновление прошло успешно.";
                        } catch (NumberFormatException e) {
                            answer = "Данные по полю coordsY не верны.";

                        }
                        return answer;
                    case ElementList.REALHERO:
                        String hero = arg.toUpperCase();
                            if (hero.equals("ДА")) {
                                humanBeing.setRealHero(true);
                                answer = "Обновление прошло успешно.";
                            } else if (hero.equals("НЕТ")) {
                                humanBeing.setRealHero(false);
                                answer = "Обновление прошло успешно.";
                            } else {
                                answer = "Данные по полю isRealHero не верны.";

                            }
                        return answer;
                    case ElementList.HASTOOTHPICK:
                        String toothPick = arg.toUpperCase();
                            switch (toothPick) {
                                case "ДА":
                                    humanBeing.setHasToothPick(true);
                                    answer = "Обновление прошло успешно.";
                                case "НЕТ":
                                    humanBeing.setHasToothPick(false);
                                    answer = "Обновление прошло успешно.";
                                case "":
                                    humanBeing.setHasToothPick(null);
                                    answer = "Обновление прошло успешно.";
                                default:
                                    answer = "Данные по полю hasToothPick не верны.";
                        }
                        return answer;
                    case ElementList.IMPACTSPEED:
                        String speed = arg;
                        if (!speed.equals("")) {
                            try {
                                    long longSpeed = Long.parseLong(speed);
                                    if (longSpeed > -902) {
                                        humanBeing.setImpactSpeed(longSpeed);
                                        answer = "Обновление прошло успешно.";
                                    } else {
                                        answer = "Данные по полю impactSpeed не верны.";
                                    }
                            } catch (NumberFormatException e) {
                                answer = "Данные по полю impactSpeed не верны.";
                            }
                        } else {
                            humanBeing.setImpactSpeed(null);
                            answer = "Обновление прошло успешно.";
                        }
                        return answer;
                    case ElementList.WEAPONTYPE:
                        String weapon = arg.toLowerCase();
                        switch (weapon) {
                            case "axe":
                                humanBeing.setWeaponType(WeaponType.AXE);
                                answer = "Обновление прошло успешно.";
                            case "pistol":
                                humanBeing.setWeaponType(WeaponType.PISTOL);
                                answer = "Обновление прошло успешно.";
                            case "shotgun":
                                humanBeing.setWeaponType(WeaponType.SHOTGUN);
                                answer = "Обновление прошло успешно.";
                            case "rifle":
                                humanBeing.setWeaponType(WeaponType.RIFLE);
                                answer = "Обновление прошло успешно.";
                            default:
                                answer = "Данные по полю weaponType не распознаны.";
                        }
                        return answer;
                    case ElementList.MOOD:
                        String moodType = arg.toLowerCase();
                        switch (moodType) {
                            case "sadness":
                                humanBeing.setMood(Mood.SADNESS);
                                answer = "Обновление прошло успешно.";
                            case "gloom":
                                humanBeing.setMood(Mood.GLOOM);
                                answer = "Обновление прошло успешно.";
                            case "apathy":
                                humanBeing.setMood(Mood.APATHY);
                                answer = "Обновление прошло успешно.";
                            case "calm":
                                humanBeing.setMood(Mood.CALM);
                                answer = "Обновление прошло успешно.";
                            case "rage":
                                humanBeing.setMood(Mood.RAGE);
                                answer = "Обновление прошло успешно.";
                            default:
                                answer = "Данные по полю mood не распознаны.";
                        }
                        return answer;
                    case ElementList.CARNAME:
                        String carname = arg;
                        if (!carname.equals("")) {
                            humanBeing.setCarName(carname);
                            answer = "Обновление прошло успешно.";
                        } else {
                            answer = "Имя машины не должно быть пустым.";
                        }
                        return answer;
                    case ElementList.CARCOOL:
                        String cool = arg.toUpperCase();

                        if (cool.equals("ДА")) {
                            humanBeing.setCarCool(true);
                            answer = "Обновление прошло успешно.";
                        } else if (cool.equals("НЕТ")) {
                            humanBeing.setCarCool(false);
                            answer = "Обновление прошло успешно.";
                        } else {
                            answer = "Данные по полю carCool не верны.";
                        }
                        return answer;
            }
        } else {
                answer = "В коллекции нет элемента с таким ID.";
                return answer;
            }
            return "";
    }

}
