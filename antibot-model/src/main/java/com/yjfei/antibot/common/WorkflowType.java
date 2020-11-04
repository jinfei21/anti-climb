package com.yjfei.antibot.common;

public enum WorkflowType {

    ROLLUP_WORKFLOW(0,"rollup"),
    RADD_WORKFLOW(1,"radd");

    private final int code;

    private final String text;

    WorkflowType(int code,String text){
        this.code = code;
        this.text = text;
    }

    public int getCode(){
        return this.code;
    }

    public String getText(){
        return this.text;
    }

    public static WorkflowType of(String name){

        for(WorkflowType type:values()){
            if (name.equalsIgnoreCase(type.getText())){
                return type;
            }
        }

        return ROLLUP_WORKFLOW;
    }
    public static WorkflowType of(int code){

        for(WorkflowType type:values()){
            if (code == type.getCode()){
                return type;
            }
        }

        return ROLLUP_WORKFLOW;
    }
}
