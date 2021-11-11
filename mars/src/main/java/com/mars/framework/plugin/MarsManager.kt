package com.mars.framework.plugin

import com.mars.components.ext.otherwise
import com.mars.components.ext.yes
import com.mars.framework.plugin.ext.log
import com.mars.framework.plugin.hook.MarsActivityManagerProxy
import com.mars.framework.plugin.hook.MarsHandlerCallback
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
        startBoolean.compareAndSet(false, true).yes {
            MarsInstrumentation().onHook()
            MarsActivityManagerProxy.onHook()
            MarsHandlerCallback.onHook()
            log(TAG, "hook success")
        }.otherwise {
            log(TAG, "has been hooked")
        }
    }

}