package com.mars.framework.plugin.utils;

import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by JohnnySwordMan on 2021/11/11
 */
public final class MethodUtil {
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final HashMap<Class<?>, Class<?>> sPrimitiveToWrapperMap = new HashMap<>();

    static {
        sPrimitiveToWrapperMap.put(Boolean.TYPE, Boolean.class);
        sPrimitiveToWrapperMap.put(Byte.TYPE, Byte.class);
        sPrimitiveToWrapperMap.put(Character.TYPE, Character.class);
        sPrimitiveToWrapperMap.put(Short.TYPE, Short.class);
        sPrimitiveToWrapperMap.put(Integer.TYPE, Integer.class);
        sPrimitiveToWrapperMap.put(Long.TYPE, Long.class);
        sPrimitiveToWrapperMap.put(Double.TYPE, Double.class);
        sPrimitiveToWrapperMap.put(Float.TYPE, Float.class);
        sPrimitiveToWrapperMap.put(Void.TYPE, Void.class);
    }

    public static Object invokeMethod(Object object, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object[] args2 = nullToEmpty(args);
        return invokeMethod(object, methodName, toClass(args2), args2);
    }

    public static Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes2 = nullToEmpty(parameterTypes);
        Object[] args2 = nullToEmpty(args);
        Method method = getAccessibleMethod(target.getClass(), methodName, parameterTypes2);
        if (method != null) {
            return method.invoke(target, args2);
        }
        return null;
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object[] args2 = nullToEmpty(args);
        return invokeStaticMethod(cls, methodName, toClass(args2), args2);
    }


    public static Object invokeStaticMethod(Class clazz, String methodName, Class<?>[] parameterTypes, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameterTypes2 = nullToEmpty(parameterTypes);
        Object[] args2 = nullToEmpty(args);
        Method method = getAccessibleMethod(clazz, methodName, parameterTypes2);
        if (method != null) {
            return method.invoke(null, args2);
        }
        return null;
    }

    private static Method getAccessibleMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        if (cls == null) {
            throw new IllegalArgumentException("the class must not be null.");
        }
        if (TextUtils.isEmpty(methodName)) {
            throw new IllegalArgumentException("the method name must not be null.");
        }
        Method targetMethod = null;
        for (Class<?> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            try {
                targetMethod = cls2.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
            }
            if (targetMethod == null) {
                Method[] declaredMethods = cls2.getDeclaredMethods();
                if (declaredMethods != null) {
                    for (Method method : declaredMethods) {
                        if (method != null && TextUtils.equals(method.getName(), methodName)) {
                            Class<?>[] paramsTypes = method.getParameterTypes();
                            if (parameterTypes != null && paramsTypes != null && parameterTypes.length == paramsTypes.length) {
                                boolean match = true;
                                for (int index = 0; index < parameterTypes.length; index++) {
                                    if (!isAssignableFrom(parameterTypes[index], paramsTypes[index])) {
                                        match = false;
                                    }
                                }
                                if (match) {
                                    targetMethod = method;
                                }
                            }
                        }
                    }
                }
            }
            if (targetMethod != null) {
                targetMethod.setAccessible(true);
                return targetMethod;
            }
        }
        return null;
    }

    private static boolean isAssignableFrom(Class<?> cls1, Class<?> cls2) {
        if (cls2 == null) {
            return false;
        }
        if (cls1 == null) {
            return !cls2.isPrimitive();
        }
        if (cls1.isPrimitive() && !cls2.isPrimitive()) {
            cls1 = sPrimitiveToWrapperMap.get(cls1);
        }
        if (cls2.isPrimitive() && !cls1.isPrimitive()) {
            cls2 = sPrimitiveToWrapperMap.get(cls2);
        }
        return cls2.isAssignableFrom(cls1);
    }

    private static Class<?>[] nullToEmpty(Class<?>[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_CLASS_ARRAY;
        }
        return array;
    }

    private static Object[] nullToEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return EMPTY_OBJECT_ARRAY;
        }
        return array;
    }

    private static Class<?>[] toClass(Object... array) {
        if (array == null || array.length == 0) {
            return EMPTY_CLASS_ARRAY;
        }
        Class<?>[] classes = new Class[array.length];
        for (int i = 0; i < array.length; i++) {
            classes[i] = array[i] == null ? null : array[i].getClass();
        }
        return classes;
    }
}
