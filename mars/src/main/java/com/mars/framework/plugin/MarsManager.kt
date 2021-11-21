package com.mars.framework.plugin

import com.mars.framework.plugin.ext.log
import com.mars.framework.plugin.hook.MarsInstrumentation
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by geyan on 2021/2/12
 *
 * 1. hook Instrumentation
 * 2. hook mH#mCallback
 */
object MarsManager {

    private const val TAG = "Mars"

    private val startBoolean = AtomicBoolean(false)

    fun startHook() {
        if (startBoolean.compareAndSet(false, true)) {
            MarsInstrumentation().onHook()
//            MarsActivityManagerProxy.onHook()
//            MarsHandlerCallback.onHook()
            log(TAG, "hook success")
        } else {
            log(TAG, "has been hooked")
        }
    }

}