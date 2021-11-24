package com.mars.framework.plugin

import android.app.Application
import android.content.Context
import com.mars.framework.plugin.classloader.MarsClassLoader
import com.mars.framework.plugin.ext.log
import com.mars.framework.plugin.hook.MarsHandlerCallback
import com.mars.framework.plugin.hook.MarsHandlerCallback.onHook
import com.mars.framework.plugin.hook.MarsHandlerCallbackV2
import com.mars.framework.plugin.hook.MarsInstrumentation
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by geyan on 2021/2/12
 *
 * 1. hook Instrumentation
 * 2. hook mH#mCallback
 */
object MarsManager {

    private const val TAG = "MarsManager"

    private val startBoolean = AtomicBoolean(false)

    private var mApp: Application? = null

    fun init(app: Application) {
        mApp = app
    }

    fun startHook() {
        if (startBoolean.compareAndSet(false, true)) {
            hookClassLoader()
            MarsInstrumentation.onHook()
//            MarsActivityManagerProxy.onHook()
            MarsHandlerCallbackV2.onHook()
//            MarsHandlerCallback.onHook()
            log(TAG, "hook success")
        } else {
            log(TAG, "has been hooked")
        }
    }

    fun hookClassLoader() {
        MarsClassLoader.onHook(mApp)
    }

}