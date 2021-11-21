package com.mars.framework.plugin.utils

import android.os.Build

/**
 * Created by JohnnySwordMan on 2021/11/21
 */
object OSUtil {

    fun isAndroidPHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
}