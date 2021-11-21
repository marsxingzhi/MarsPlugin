package com.mars.framework.plugin.reflect;

import java.lang.reflect.Method;

/**
 * Created by JohnnySwordMan on 2021/11/21
 */
public final class MethodUtils {

    public static Object invokeMethod(Object obj, String methodName) {
        return invokeMethod(obj, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
        if (obj == null)
            return null;
        try {
            //调用一个private方法
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes); //在指定类中获取指定的方法
            method.setAccessible(true);
            return method.invoke(obj, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object invokeStaticMethod(String className, String methodName) {
        return invokeStaticMethod(className, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName) {
        return invokeStaticMethod(cls, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invokeStaticMethod(String className, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
        try {
            Class<?> cls = Class.forName(className);
            return invokeStaticMethod(cls, methodName, parameterTypes, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(Class<?> cls, String methodName, Class<?>[] parameterTypes, Object[] parameterValues) {
        try {
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(null, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
