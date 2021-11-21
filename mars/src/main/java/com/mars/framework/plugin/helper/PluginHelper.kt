package com.mars.framework.plugin.helper

import com.mars.framework.plugin.Constants
import com.mars.framework.plugin.reflect.MethodUtils
import com.mars.framework.plugin.utils.MethodUtil

/**
 * Created by JohnnySwordMan on 2021/11/11
 */
object PluginHelper {

    private var mActivityThread: Any? = null

    fun getCurrentActivityThread(): Any {
        if (mActivityThread == null) {
            synchronized(PluginHelper::class.java) {
                if (mActivityThread == null) {
                    val cls = Class.forName(Constants.CLASS_NAME_ACTIVITY_THREAD)
//                    mActivityThread = MethodUtil.invokeStaticMethod(cls, "currentActivityThread", null)
                    mActivityThread = MethodUtils.invokeStaticMethod(cls, "currentActivityThread")
                }
            }
        }
        return mActivityThread!!
    }
}