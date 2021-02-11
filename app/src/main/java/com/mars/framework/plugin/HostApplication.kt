package com.mars.framework.plugin

import android.app.Application
import android.content.Context
import kotlin.Exception

/**
 * Created by geyan on 2021/2/12
 */

class HostApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        try {
            Mars.startHook()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}