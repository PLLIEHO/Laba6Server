package server.core;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Saver {
    private String body = "";
    public void save(String fileName, Collection collection) throws IOException {
        File file = new File(fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
            body = body + "<" + TagList.HEROLIST + ">" + "\n";
            collection.getCollection().forEach(human -> {
                body = body + "<" + TagList.HUMANBEING + ">" + "\n";
                body = body + "<" + TagList.ID + ">" + human.getId() + "<" + TagList.ID_OVER + ">" + "\n";
                body = body + "<" + TagList.NAME + ">" + human.getName() + "<" + TagList.NAME_OVER + ">" + "\n";
                body = body + "<" + TagList.COORDINATES + ">" + "\n";
                body = body + "<" + TagList.X + ">" + human.getCoordinatesX() + "<" + TagList.X_OVER + ">" + "\n";
                body = body + "<" + TagList.Y + ">" + human.getCoordinatesY() + "<" + TagList.Y_OVER + ">" + "\n";
                body = body + "<" + TagList.COORDINATES_OVER + ">" + "\n";
                body = body + "<" + TagList.CREATION_DATE + ">" + human.getCreationDate().getTime() + "<" + TagList.CREATION_DATE_OVER + ">" + "\n";
                body = body + "<" + TagList.REAL_HERO + ">" + human.getRealHero().toString() + "<" + TagList.REAL_HERO_OVER + ">" + "\n";
                if (human.getHasToothPick() == null) {
                    body = body + "<" + TagList.HAS_TOOTHPICK + ">" + "" + "<" + TagList.HAS_TOOTHPICK_OVER + ">" + "\n";
                } else if (human.getHasToothPick().toString().equals("true") || human.getHasToothPick().equals("false")) {
                    body = body + "<" + TagList.HAS_TOOTHPICK + ">" + human.getHasToothPick().toString() + "<" + TagList.HAS_TOOTHPICK_OVER + ">" + "\n";
                }
                if (human.getImpactSpeed() != null) {
                    body = body + "<" + TagList.IMPACT_SPEED + ">" + human.getImpactSpeed() + "<" + TagList.IMPACT_SPEED_OVER + ">" + "\n";
                } else {
                    body = body + "<" + TagList.IMPACT_SPEED + ">" + "" + "<" + TagList.IMPACT_SPEED_OVER + ">" + "\n";
                }
                body = body + "<" + TagList.WEAPON_TYPE + ">" + human.getWeaponType().toString() + "<" + TagList.WEAPON_TYPE_OVER + ">" + "\n";
                body = body + "<" + TagList.MOOD + ">" + human.getMood().toString() + "<" + TagList.MOOD_OVER + ">" + "\n";
                body = body + "<" + TagList.CAR + ">" + "\n";
                body = body + "<" + TagList.NAME + ">" + human.getCarName() + "<" + TagList.NAME_OVER + ">" + "\n";
                body = body + "<" + TagList.CAR_COOL + ">" + human.getCarCool() + "<" + TagList.CAR_COOL_OVER + ">" + "\n";
                body = body + "<" + TagList.CAR_OVER + ">" + "\n";
                body = body + "<" + TagList.HUMANBEING_OVER + ">" + "\n";
            });
            body = body + "<" + TagList.HEROLIST_OVER + ">";

            fileOutputStream.write(body.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        } catch(FileNotFoundException e){
            if(file.exists()) {
                System.out.println("Файл заблокирован для записи.");
            }else{System.out.println("Файл перемещён или удалён.");
            }
            System.exit(1);
        }
    }
}