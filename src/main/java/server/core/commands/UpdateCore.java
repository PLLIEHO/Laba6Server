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
                            humanBeing.setRealHero(Boolean.parseBoolean(arg));
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
                            return answer;
                        case ElementList.HASTOOTHPICK:
                            humanBeing.setHasToothPick(Boolean.parseBoolean(arg));
                            new DBConnection().updateDB(humanBeing, user);
                            answer = "Обновление прошло успешно.";
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
                            humanBeing.setWeaponType(WeaponType.parseWeaponType(weapon));
                            answer = "Обновление прошло успешно.";
                            new DBConnection().updateDB(humanBeing, user);
                            return answer;
                        case ElementList.MOOD:
                            String moodType = arg.toLowerCase();
                            humanBeing.setMood(Mood.parseMood(moodType));
                            answer = "Обновление прошло успешно.";
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
                            humanBeing.setCarCool(Boolean.parseBoolean(cool));
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


