package server.data;

public class Car {
    private String name;
    private boolean cool;

    public void setName(String name){
        this.name = name;
    }
    public void setCool(Boolean cool){
        this.cool = cool;
    }
    public String getName(){return name;}
    public boolean getCool(){return cool;}
}
