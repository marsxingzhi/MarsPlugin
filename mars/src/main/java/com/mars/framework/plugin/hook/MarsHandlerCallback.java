package com.mars.framework.plugin.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.mars.framework.plugin.Constants;
import com.mars.framework.plugin.PluginHelperKt;
import com.mars.framework.plugin.reflect.FieldUtils;

/**
 * Created by geyan on 2021/2/12
 */
public class MarsHandlerCallback implements Handler.Callback {

    /**
     * 由于之前用StubActivity欺骗了AMS，现在需要还原回真正启动的插件Activity，
     * 最终会交给ActivityThread的内部类H来启动Activity，H会完成消息转发，最终调用其callback
     */
    public static void onHook() {
        Object sCurrentActivityThread = PluginHelperKt.getCurrentActivityThread();
        // 一个进程只有一个ActivityThread，获取该对象的mH
        Handler mH = (Handler) FieldUtils.readField(sCurrentActivityThread, Constants.FIELD_NAME_M_H);
        // 只要接口才能使用动态代理，这里直接构建对象就可以
        FieldUtils.writeField(Handler.class, mH, Constants.FIELD_NAME_M_CALLBACK, new MarsHandlerCallback(mH));
    }

    private Handler target;

    public MarsHandlerCallback(Handler base) {
        this.target = base;
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 100:
                handleLaunchActivity(msg);
                break;
            default:
                break;
        }
        target.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        Object obj = msg.obj;
        Intent raw = (Intent) FieldUtils.readField(obj, Constants.FILED_NAME_INTENT);
        Intent targetIntent = raw.getParcelableExtra(Constants.EXTRA_TARGET_INTENT);
        // 如果Hook操作是在Application中调用的，那么targetIntent需要判空；如果放在MainActivity中执行，则无需判空
        if (targetIntent == null) {
            return;
        }
        raw.setComponent(targetIntent.getComponent());
    }
}
