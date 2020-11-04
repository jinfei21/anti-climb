package com.yjfei.antibot.common;

public enum RollupType {


    TUMBLE_WINDOW(1,"tumblevalue"),
    TUMBLE_RADD(2,"tumblecount");

    private int code;
    private String text;


    RollupType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static RollupType of(int code){

        for (RollupType rollupType:values()){
            if (code == rollupType.code){
                return rollupType;
            }
        }
        return null;
    }

    public static RollupType of(String name){

        for (RollupType rollupType:values()){
            if (rollupType.text.equalsIgnoreCase(name)){
                return rollupType;
            }
        }

        return null;
    }
}
