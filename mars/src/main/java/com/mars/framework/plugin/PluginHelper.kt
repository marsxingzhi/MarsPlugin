package com.mars.framework.plugin

import android.content.Context
import com.mars.components.util.reflect.FieldUtils

/**
 * Created by geyan on 2021/2/10
 */

/**
 * 是否在主进程
 */
fun isMainProcess(context: Context?): Boolean {
    context ?: return false
    return true
}

private var sActivityThread: Any? = null

fun getCurrentActivityThread(): Any {
    if (sActivityThread == null) {
        sActivityThread =
            FieldUtils.readStaticField(Constants.CLS_NAME_ACTIVITY_THREAD, Constants.FIELD_NAME_CURRENT_ACTIVITY_THREAD)
    }
    return sActivityThread!!
}