package com.yjfei.antibot.common;

public enum DataSourceType {
    ONS(0,"ons"),
    MYSQL(1,"mysql");

    private final int code;

    private final String text;

    DataSourceType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static DataSourceType of(String name){

        for(DataSourceType type:values()){
            if (name.equalsIgnoreCase(type.getText())){
                return type;
            }
        }

        return ONS;
    }
    public static DataSourceType of(int code){

        for(DataSourceType type:values()){
            if (code == type.getCode()){
                return type;
            }
        }

        return ONS;
    }

}
