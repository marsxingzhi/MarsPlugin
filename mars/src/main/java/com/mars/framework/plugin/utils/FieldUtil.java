package com.mars.framework.plugin.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.core.util.Preconditions;

import java.lang.reflect.Field;

/**
 * Created by JohnnySwordMan on 2021/11/11
 */
public final class FieldUtil {

    @SuppressLint("RestrictedApi")
    public static Object readField(Field field, Object target) throws IllegalAccessException {
        Preconditions.checkArgument(field != null, "the field must not be null.");
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(target);
    }

    @SuppressLint("RestrictedApi")
    public static Object readField(Object target, String fieldName) throws IllegalAccessException {
        Preconditions.checkNotNull(target, "the target must not be null.");
        Field field = getField(target.getClass(), fieldName);
        if (field != null) {
            return readField(field, target);
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    public static Object readStaticField(Field field) throws IllegalAccessException {
        Preconditions.checkNotNull(field, "the field must not be null.");
        return readField(null, field);
    }

    public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
        Field field = getField(cls, fieldName);
        if (field != null) {
            return readStaticField(field);
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    public static void writeField(Field field, Object target, Object value) throws IllegalAccessException {
        Preconditions.checkNotNull(field, "the field must not be null.");
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(target, value);
    }

    @SuppressLint("RestrictedApi")
    public static void writeField(String fieldName, Object target, Object value) throws IllegalAccessException {
        Preconditions.checkNotNull(target, "the target must not be null.");
        Field field = getField(target.getClass(), fieldName);
        if (field != null) {
            writeField(field, target, value);
        }
    }

    @SuppressLint("RestrictedApi")
    public static void writeStaticField(Field field, Object value) throws IllegalAccessException {
        Preconditions.checkNotNull(field, "the field must not be null.");
        writeField(field, null, value);
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
        Field field = getField(cls, fieldName);
        if (field != null) {
            writeStaticField(field, value);
        }
    }


    @SuppressLint("RestrictedApi")
    private static Field getField(Class<?> cls, String fieldName) {
        Preconditions.checkNotNull(cls, "the class must not be null.");
        if (!TextUtils.isEmpty(fieldName)) {
            throw new IllegalArgumentException("the field name must not be null.");
        }
        while (cls != null) {
            try {
                Field field = cls.getDeclaredField(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }
}
