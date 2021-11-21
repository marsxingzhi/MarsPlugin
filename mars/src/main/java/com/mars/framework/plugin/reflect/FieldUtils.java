package com.mars.framework.plugin.reflect;

import java.lang.reflect.Field;

/**
 * Created by JohnnySwordMan on 2021/11/21
 */
public final class FieldUtils {

    /**
     * @param target    反射的对象
     * @param fieldName 反射对象的字段
     * @return 返回字段对应的值
     */
    public static Object readField(Object target, String fieldName) {
        return readField(target.getClass(), target, fieldName);
    }


    public static Object readField(Class<?> cls, Object target, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object readField(String className, Object target, String fieldName) {
        try {
            Class<?> cls = Class.forName(className);
            return readField(cls, target, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object readStaticField(String className, String fieldName) {
        return readField(className, null, fieldName);
    }

    public static Object readStaticField(Class<?> cls, String fieldName) {
        return readField(cls, null, fieldName);
    }


    public static void writeField(Object target, String fieldName, Object fieldValue) {
        writeField(target.getClass(), target, fieldName, fieldValue);
    }

    public static void writeField(Class<?> cls, Object target, String fieldName, Object fieldValue) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeField(String className, Object target, String fieldName, Object fieldValue) {
        try {
            Class<?> cls = Class.forName(className);
            writeField(cls, target, fieldName, fieldValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeStaticField(String classname, String fieldName, Object fieldValue) {
        writeField(classname, null, fieldName, fieldValue);
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object fieldValue) {
        writeField(cls, null, fieldName, fieldValue);
    }


}
