package com.yjfei.antibot.common;

public enum RiskType {
    LOW(0,"low"),
    MID(1,"mid"),
    HIGH(2,"high");
    private int code;
    private String text;


    RiskType(int code,String text){
        this.code = code;
        this.text = text;
    }


    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }
}
