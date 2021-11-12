package com.mars.framework.plugin.hook;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;

import com.mars.components.util.reflect.FieldUtils;
import com.mars.framework.plugin.Constants;
import com.mars.framework.plugin.stub.StubStandardActivity0;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by geyan on 2021/2/12
 */
public class MarsActivityManagerProxy implements InvocationHandler {

    /**
     * Hook AMS
     * 主要完成的操作：把真正要启动的Activity临时替换为在AndroidManifest.xml中声明的替身Activity，进而骗过AMS
     * https://www.androidos.net.cn/android/7.0.0_r31/xref/frameworks/base/core/java/android/app/ActivityManagerNative.java
     */
    public static void onHook() throws ClassNotFoundException {
        // 获取AMN的gDefault单例，final静态的
        Object gDefault = FieldUtils.readStaticField(Constants.CLS_NAME_ACTIVITY_MANAGER_NATIVE, Constants.FIELD_NAME_G_DEFAULT);
        // gDefault是一个Singleton对象，取出这个单例里面的mInstance字段，这个字段其实是IActivityManagerNative的实现类
        Object mInstance = FieldUtils.readField(Constants.CLS_NAME_SINGLETON, gDefault, Constants.FIELD_NAME_M_INSTANCE);

        @SuppressLint("PrivateApi")
        Class<?> cls = Class.forName(Constants.INTERFACE_NAME_I_ACTIVITY_MANAGER);
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{cls},
                new MarsActivityManagerProxy(mInstance));
        // 把gDefault的mInstance字段，修改为proxy
        FieldUtils.writeField(Constants.CLS_NAME_SINGLETON, gDefault, Constants.FIELD_NAME_M_INSTANCE, proxy);
    }

    Object target;

    public MarsActivityManagerProxy(Object base) {
        target = base;
    }

    /**
     * 注：如果已经hook了，还是报是否在AndroidManifest.xml中注册Activity，很大概率就是包名写的不对！
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            int idx = checkIntent(args);
            Intent originIntent = (Intent) args[idx];
            Intent newIntent = new Intent();

            // 这里我们把启动的Activity临时替换为 StubActivity
            ComponentName componentName = new ComponentName(Constants.STUB_PACKAGE_NAME, StubStandardActivity0.class.getName());
            newIntent.setComponent(componentName);
            newIntent.putExtra(Constants.EXTRA_TARGET_INTENT, originIntent);
            args[idx] = newIntent;
        }
        return method.invoke(target, args);
    }

    private int checkIntent(Object[] args) {
        int idx = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                idx = i;
                break;
            }
        }
        return idx;
    }
}
