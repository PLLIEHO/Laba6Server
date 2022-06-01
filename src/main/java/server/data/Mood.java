package server.data;

public enum Mood {
    SADNESS,
    GLOOM,
    APATHY,
    CALM,
    RAGE;

    public static Mood parseMood(String s){
        switch (s.toUpperCase()){
            case "SADNESS":
                return SADNESS;
            case "GLOOM":
                return GLOOM;
            case "APATHY":
                return APATHY;
            case "CALM":
                return CALM;
            case "RAGE":
                return RAGE;
            default:
                return null;
        }
    }
}
