package com.mo.medreminder;

public class Alarm {

    private int id;
    private String name;
    private  String  dose;
    private String  time;
    private int reqCode;


    public Alarm(String name,String dose, String time , int reqCode) {
        this.name = name;
        this.dose = dose;
        this.time = time;
        this.reqCode = reqCode;
    }

    public int getReqCode() {
        return reqCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDose() {
        return dose;
    }

    public String getTime() {
        return time;
    }

}
