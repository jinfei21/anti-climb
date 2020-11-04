package com.yjfei.antibot.stream.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConvertUtil {

    public static <S, T> T convert(S s, Class<T> tClass,String... ignoreProperties) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("convert error.", e);
        }
    }

    public static <S, T> List<T> convert(List<S> list, Function<? super S, ? extends T> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }


    public static <S, T> List<T> convert(Iterable<S> iterable, Function<? super S, ? extends T> mapper) {
        return StreamSupport.stream(iterable.spliterator(), false).map(mapper).collect(Collectors.toList());
    }
}