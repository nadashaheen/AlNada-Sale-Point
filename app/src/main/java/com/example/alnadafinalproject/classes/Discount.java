package com.example.alnadafinalproject.classes;

public class Discount {

    String id ;
    String code ;
    String percent ;

    public Discount(){}

    public Discount(String id, String code, String percent) {
        this.id = id;
        this.code = code;
        this.percent = percent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
