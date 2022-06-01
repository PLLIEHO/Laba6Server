package server.core;



import server.Server;
import server.data.HumanBeing;
import server.data.Mood;
import server.data.WeaponType;
import server.exceptions.FileErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {
    private Pattern XMLinfo = Pattern.compile("<\\?([\\s\\S]+?)\\?>");
    private Pattern basic = Pattern.compile("<([\\s\\S]+?)>");
    private Pattern value = Pattern.compile(">([\\s\\S]*?)<");
    private boolean infoFlag = true;
    private static String info;
    private int count = 0;
    private int carFlag = 0;
    private boolean idFlag = true;
    private Map<String, Boolean> checklist = new HashMap<>();
    private boolean sygnal;
    private int counter;
    private Collection collection;
    public Parser(Collection collection){
        this.collection = collection;
    }

    public void start(String fileName) throws IOException {
        Server.LOG.info("Парсер приступает к расшифровке файла {}", fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        if(lineCounter(fileName)>2) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(TagList.HEROLIST)) {
                    this.execute(line);
                }
            }
        } else {
            Server.LOG.info("Файл {} пуст или содержит недостаточно информации.", fileName);
        }
    }

    public int lineCounter(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String lin;
        int counter = 0;
        while ((lin = reader.readLine()) != null) {
            counter += 1;
        }
        reader.close();
        this.counter = counter;
        return counter;
    }

    public void execute(String line){
        count += 1;
        if(infoFlag) {
            info = line;
            infoFlag = false;
        }
        else{
            Matcher matcherA = basic.matcher(line);
            Matcher matcherB = value.matcher(line);
            if(matcherA.find()) {
                String tag = matcherA.group().replace("<", "").replace(">", "");
                if (idFlag&&!tag.equals(TagList.HEROLIST_OVER)&&count<counter-1) {
                    if (check()) {
                        HumanBeing human = new HumanBeing();
                        collection.addHuman(human);
                        sygnal = false;
                        idFlag = false;
                    }
                }
                else if(sygnal&&!tag.equals(TagList.HEROLIST_OVER)&&!check()) {
                    collection.getCollection().removeLast();
                    sygnal = false;
                    Server.LOG.info("В файле ошибка. Проверьте целостность данных и количество аргументов.");
                    throw new FileErrorException("В файле ошибка. Проверьте целостность данных и количество аргументов.");
                }
                else {
                    switch (tag) {
                        case TagList.ID:
                            this.idParse(matcherB);
                            break;
                        case TagList.NAME:
                            this.nameParse(matcherB);
                            break;
                        case TagList.COORDINATES:
                            if(checklist.get("coordinates") ==null) {
                                checklist.put("coordinates", true);
                                break;
                            }
                            break;
                        case TagList.X:
                            if(checklist.get("coordinates")) {
                                this.coordinatesXParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.Y:
                            if(checklist.get("coordinates")) {
                                this.coordinatesYParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.COORDINATES_OVER:
                            break;
                        case TagList.CREATION_DATE:
                            if(checklist.get("creationdate")==null) {
                                this.creationDateParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.REAL_HERO:
                            if(checklist.get("realhero")==null) {
                                this.realHeroParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.HAS_TOOTHPICK:
                            if(checklist.get("hastoothpick")==null) {
                                this.hasToothpickParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.IMPACT_SPEED:
                            if(checklist.get("impactspeed")==null) {
                                this.impactSpeedParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.WEAPON_TYPE:
                            if(checklist.get("weapontype")==null) {
                                this.weaponTypeParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.MOOD:
                            if(checklist.get("mood")==null) {
                                this.moodParse(matcherB);
                                break;
                            }

                            break;
                        case TagList.CAR:
                            if(checklist.get("car")==null) {
                                carFlag = carFlag + 1;
                                checklist.put("car", true);
                                break;
                            }
                            break;
                        case TagList.CAR_COOL:
                            if(checklist.get("carcool")==null) {
                                this.carCoolParse(matcherB);
                                break;
                            }
                            break;
                        case TagList.CAR_OVER:
                            carFlag = carFlag - 1;
                            break;
                        case TagList.HUMANBEING_OVER:
                            sygnal = true;
                            idFlag = true;
                            break;
                        default:
                            //sygnal = true;
                            //idFlag = true;
                            break;
                    }
                    // }
                }
            }
        }
    }
    public static String getInfo(){
        return info;
    }
    public boolean check(){
        if(checklist.size()==14){
            checklist.clear();
            return (true);
        } else if(checklist.size()==0){
            return(true);
        }
        else if(checklist.size()<13){
            checklist.clear();
            return (false);
        }
        return (false);
    }

    private void idParse(Matcher matcherB){
        if (matcherB.find()) {
            String idString = matcherB.group().replace("<", "").replace(">", "");
            try {
                long id = Long.parseLong(idString);
                checklist.put("ID", true);
                collection.getHuman().setId(id);
            } catch (NumberFormatException ignored){

            }
        }
    }

    private void nameParse(Matcher matcherB){
        if (matcherB.find()) {
            String name = matcherB.group().replace("<", "").replace(">", "");
            if (carFlag == 0) {
                if(checklist.get("name")==null) {
                    if (!name.equals("")) {
                        collection.getHuman().setName(name);
                        checklist.put("name", true);
                    }
                }
            } else if (carFlag == 1&&checklist.get("carname")==null) {
                collection.getHuman().setCarName(name);
                checklist.put("carname", true);
            }
        }
    }

    private void coordinatesXParse(Matcher matcherB){
        if (matcherB.find()) {
            String x = matcherB.group().replace("<", "").replace(">", "");
            try {
                float FloatX = Float.parseFloat(x);
                if (FloatX > -316) {
                    collection.getHuman().setCoordinatesX(FloatX);
                    checklist.put("x", true);
                }
            } catch (NumberFormatException ignored){

            }
        }
    }

    private void coordinatesYParse(Matcher matcherB){
        if (matcherB.find()) {
            String y = matcherB.group().replace("<", "").replace(">", "");
            try {
                double DoubleY = Double.parseDouble(y);
                collection.getHuman().setCoordinatesY(DoubleY);
                checklist.put("y", true);
            } catch (NumberFormatException ignored){
            }
        }
    }

    private void creationDateParse(Matcher matcherB){
        if (matcherB.find()) {
            String date = matcherB.group().replace("<", "").replace(">", "");
            Date data = new Date(Long.parseLong(date));
            collection.getHuman().setCreationDate(data);
            checklist.put("creationdate", true);
        }
    }

    private void realHeroParse(Matcher matcherB){
        if (matcherB.find()) {
            String hero = matcherB.group().replace("<", "").replace(">", "");
            if (hero.equals("true")) {
                collection.getHuman().setRealHero(true);
                checklist.put("realhero", true);
            } else if (hero.equals("false")) {
                collection.getHuman().setRealHero(false);
                checklist.put("realhero", true);
            }
        }
    }

    private void hasToothpickParse(Matcher matcherB){
        if (matcherB.find()) {
            String toothPick = matcherB.group().replace("<", "").replace(">", "");
            if (toothPick.equals("true")) {
                collection.getHuman().setHasToothPick(true);
                checklist.put("hastoothpick", true);
            } else if (toothPick.equals("false")) {
                collection.getHuman().setHasToothPick(false);
                checklist.put("hastoothpick", true);
            }
        }
    }

    private void impactSpeedParse(Matcher matcherB){
        if (matcherB.find()) {
            String speed = matcherB.group().replace("<", "").replace(">", "");
            if (!speed.equals("")) {
                Long impactSpeed = Long.parseLong(speed);
                collection.getHuman().setImpactSpeed(impactSpeed);
            } else {
                collection.getHuman().setImpactSpeed(null);
            }
            checklist.put("impactspeed", true);
        }
    }

    private void weaponTypeParse(Matcher matcherB){
        if (matcherB.find()) {
            String type = matcherB.group().replace("<", "").replace(">", "");
            switch (type) {
                case "AXE":
                    collection.getHuman().setWeaponType(WeaponType.AXE);
                    checklist.put("weapontype", true);
                    break;
                case "PISTOL":
                    collection.getHuman().setWeaponType(WeaponType.PISTOL);
                    checklist.put("weapontype", true);
                    break;
                case "SHOTGUN":
                    collection.getHuman().setWeaponType(WeaponType.SHOTGUN);
                    checklist.put("weapontype", true);
                    break;
                case "RIFLE":
                    collection.getHuman().setWeaponType(WeaponType.RIFLE);
                    checklist.put("weapontype", true);
                    break;
            }
        }
    }

    private void moodParse(Matcher matcherB) {
        if (matcherB.find()) {
            String mood = matcherB.group().replace("<", "").replace(">", "");
            switch (mood) {
                case "SADNESS":
                    collection.getHuman().setMood(Mood.SADNESS);
                    checklist.put("mood", true);
                    break;
                case "GLOOM":
                    collection.getHuman().setMood(Mood.GLOOM);
                    checklist.put("mood", true);
                    break;
                case "APATHY":
                    collection.getHuman().setMood(Mood.APATHY);
                    checklist.put("mood", true);
                    break;
                case "CALM":
                    collection.getHuman().setMood(Mood.CALM);
                    checklist.put("mood", true);
                    break;
                case "RAGE":
                    collection.getHuman().setMood(Mood.RAGE);
                    checklist.put("mood", true);
                    break;
            }
        }
    }

    private void carCoolParse(Matcher matcherB){
        if (matcherB.find()) {
            String cool = matcherB.group().replace("<", "").replace(">", "");
            if (cool.equals("true")) {
                collection.getHuman().setCarCool(true);
                checklist.put("carcool", true);
            } else if (cool.equals("false")) {
                collection.getHuman().setCarCool(false);
                checklist.put("carcool", true);
            }

        }
    }

}
