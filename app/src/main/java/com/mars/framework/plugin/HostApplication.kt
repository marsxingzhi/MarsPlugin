package com.mars.framework.plugin

import android.app.Application
import android.content.Context
import kotlin.Exception

/**
 * Created by geyan on 2021/2/12
 */

class HostApplication : Application() {

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
//        DownloadApkHelper.mockDownloadPluginApk(context!!, "tool.apk")
        MarsManager.init(this)
        MarsManager.startHook()
        // 暂时先别加try-catch，尽可能暴露问题
//        try {
//            MarsManager.startHook()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }
}