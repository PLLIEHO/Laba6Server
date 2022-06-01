package server.data;

public enum WeaponType {
    AXE,
    PISTOL,
    SHOTGUN,
    RIFLE;

    public static WeaponType parseWeaponType(String s){
        switch (s.toUpperCase()){
            case "AXE":
                return AXE;
            case "PISTOL":
                return PISTOL;
            case "SHOTGUN":
                return SHOTGUN;
            case "RIFLE":
                return RIFLE;
            default:
                return null;
        }
    }
}
