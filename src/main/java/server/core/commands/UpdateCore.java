package server.core.commands;



import common.ElementList;
import server.DBConnection;
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

    public String update(String user){
        String answer;
        String tag = element.get(0);
        String arg = element.get(1);
        List<HumanBeing> human = collection.getCollection().stream().filter(s -> s.getId()==ID).collect(Collectors.toList());
            if (human.size()>0) {
                HumanBeing humanBeing = human.get(0);
                if (humanBeing.getUser().equals(user)) {
                    switch (tag) {
                        case ElementList.NAME:
                            String name = arg;
                            humanBeing.setName(name);
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.COORDSX:
                            String x = arg;
                            float floatX = Float.parseFloat(x);
                            humanBeing.setCoordinatesX(floatX);
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.COORDSY:
                            String y = arg;
                            humanBeing.setCoordinatesY(Double.parseDouble(y));
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.REALHERO:
                            String hero = arg.toUpperCase();
                            if (hero.equals("true")) {
                                humanBeing.setRealHero(true);
                            } else {
                                humanBeing.setRealHero(false);
                            }
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.HASTOOTHPICK:
                            String toothPick = arg.toUpperCase();
                            switch (toothPick) {
                                case "true":
                                    humanBeing.setHasToothPick(true);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "false":
                                    humanBeing.setHasToothPick(false);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                default:
                                    humanBeing.setHasToothPick(null);
                                    new DBConnection().updateDB(humanBeing, user);
                                    answer = "Обновление прошло успешно.";
                                    break;
                            }
                            return answer;
                        case ElementList.IMPACTSPEED:
                            String speed = arg;
                            if (!speed.equals("null")) {
                                long longSpeed = Long.parseLong(speed);
                                humanBeing.setImpactSpeed(longSpeed);
                            } else {
                                humanBeing.setImpactSpeed(null);
                            }
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.WEAPONTYPE:
                            String weapon = arg.toLowerCase();
                            switch (weapon) {
                                case "axe":
                                    humanBeing.setWeaponType(WeaponType.AXE);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "pistol":
                                    humanBeing.setWeaponType(WeaponType.PISTOL);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "shotgun":
                                    humanBeing.setWeaponType(WeaponType.SHOTGUN);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                default:
                                    humanBeing.setWeaponType(WeaponType.RIFLE);
                                    answer = "Обновление прошло успешно.";
                                    break;
                            }
                            new DBConnection().updateDB(humanBeing, user);
                            return answer;
                        case ElementList.MOOD:
                            String moodType = arg.toLowerCase();
                            switch (moodType) {
                                case "sadness":
                                    humanBeing.setMood(Mood.SADNESS);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "gloom":
                                    humanBeing.setMood(Mood.GLOOM);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "apathy":
                                    humanBeing.setMood(Mood.APATHY);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                case "calm":
                                    humanBeing.setMood(Mood.CALM);
                                    answer = "Обновление прошло успешно.";
                                    break;
                                default:
                                    humanBeing.setMood(Mood.RAGE);
                                    answer = "Обновление прошло успешно.";
                            }
                            new DBConnection().updateDB(humanBeing, user);
                            return answer;
                        case ElementList.CARNAME:
                            String carname = arg;
                            humanBeing.setCarName(carname);
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.CARCOOL:
                            String cool = arg.toUpperCase();
                            if (cool.equals("true")) {
                                humanBeing.setCarCool(true);
                            } else {
                                humanBeing.setCarCool(false);
                            }
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                    }
                } else {
                    return "You dont have access to this object.";
                }
                } else {
                    answer = "В коллекции нет элемента с таким ID.";
                    return answer;
                }
                return "";
            }
    }


