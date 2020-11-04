package com.yjfei.antibot.common;

import java.util.Arrays;
import java.util.Optional;

public enum VariableType {

    OTF(0,"OTF"),
    NAMELIST(1,"NameList"),
    RADD(2, "Radd"),
    ROLLUP(3, "Rollup");


    private final int code;
    private final String text;

    VariableType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }


    public static Optional<VariableType> of(int code) {
        return Arrays.stream(VariableType.values()).filter(variableType -> variableType.code == code).findFirst();
    }

    public static VariableType toType(int code){

        for (VariableType type:values()){
            if (type.getCode() == code){
                return type;
            }
        }
        return RADD;
    }

    public static VariableType toType(String name){
        for (VariableType type:values()){
            if (type.text.equalsIgnoreCase(name)){
                return type;
            }
        }
        return RADD;
    }
}
