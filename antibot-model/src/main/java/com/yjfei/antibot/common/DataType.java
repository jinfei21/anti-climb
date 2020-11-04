package com.yjfei.antibot.common;

import java.util.Optional;

/**
 * 数据类型
 */
public enum DataType {

    LONG(0,"long"),
    DOUBLE(1,"double"),
    STRING(2,"string"),
    LIST(3,"list"),
    SET(4,"set"),
    MAP(5,"map"),
    OBJECT(6,"object");

    private int code;
    private String text;


    DataType(int code,String text){
        this.code = code;
        this.text = text;
    }

    /**
     * 将数据库中的编码翻译为本枚举
     *
     * @param 类型code
     * @return 对应的枚举
     */
    public static Optional<DataType> of(int code) {


        for (DataType dataType:values()){
            if (dataType.code == code){
                return Optional.of(dataType);
            }
        }
        return Optional.empty();

    }

    public static DataType of(String name){
        for(DataType type:values()){
            if (type.text.equalsIgnoreCase(name)){
                return type;
            }
        }
        return STRING;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }
}
