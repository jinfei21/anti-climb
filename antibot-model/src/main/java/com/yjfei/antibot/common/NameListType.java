package com.yjfei.antibot.common;

public enum NameListType {

    BLACK_LIST(0,"黑名单"),
    WHITE_LIST(1,"白名单");

    private int code;
    private String text;

    NameListType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static NameListType of(String name){
        for (NameListType type:values()){
            if (type.text.equalsIgnoreCase(name)){
                return type;
            }
        }
       return  null;
    }

    public static NameListType of(int code){
        for (NameListType type:values()){
            if (type.code == code){
                return type;
            }
        }
        return  BLACK_LIST;
    }
}
