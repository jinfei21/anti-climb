package com.yjfei.antibot.engine.rule;


import java.util.function.BiFunction;

public class main {

    @FunctionalInterface
    public interface A{
        String a(String a,String b);
       default String test( String a){
            return a+1+a("a","bbb");
        }
    }

    interface BiOp extends BiFunction<A,String,String>{

    }

    public static void main(String args[]){

       String str =  ((BiOp)A::test).apply((a,b)->{
           a = a+b;
           return "ok:"+a;
       },"a");


       System.out.println(str);
        System.out.println(str);

    }
}
