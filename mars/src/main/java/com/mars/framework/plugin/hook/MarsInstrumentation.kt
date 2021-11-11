package com.mars.framework.plugin.hook

import android.app.Instrumentation
import com.mars.framework.plugin.ext.log
import com.mars.framework.plugin.helper.PluginHelper
import com.mars.framework.plugin.utils.FieldUtil

/**
 * Created by JohnnySwordMan on 2021/11/11
 */
class MarsInstrumentation : Instrumentation(), IHook {

    companion object {
        const val TAG = "MarsInstrumentation"
    }

    var mOrigin: Instrumentation? = null

    /**
     * 替换ActivityThread的mInstrumentation
     */
    override fun onHook() {
        val currentActivityThread = PluginHelper.getCurrentActivityThread()
        val mInstrumentationField = FieldUtil.readField(currentActivityThread, "mInstrumentation")
        if (mInstrumentationField !is MarsInstrumentation) {
            mOrigin = mInstrumentationField as Instrumentation
            FieldUtil.writeField("mInstrumentation", currentActivityThread, this)
            log(TAG, "hook success")
        }
    }
}