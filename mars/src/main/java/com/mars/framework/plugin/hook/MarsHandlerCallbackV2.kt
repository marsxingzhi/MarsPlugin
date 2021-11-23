package com.mars.framework.plugin.hook

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import com.mars.framework.plugin.Constants
import com.mars.framework.plugin.helper.PluginHelper
import com.mars.framework.plugin.reflect.FieldUtils

/**
 * Created by JohnnySwordMan on 2021/11/22
 */
class MarsHandlerCallbackV2() : Handler.Callback {

    companion object {

        const val TAG = "MarsHandlerCallbackV2"

        const val LAUNCH_ACTIVITY = 100
        // Android P去掉了100-109这10个用于Activity的消息，都改成了159
        const val EXECUTE_TRANSACTION = 159


        fun onHook() {
            val sCurrentActivityThread = PluginHelper.getCurrentActivityThread()
            // 一个进程只有一个ActivityThread，获取该对象的mH
            val mH = FieldUtils.readField(sCurrentActivityThread, Constants.FIELD_NAME_M_H) as Handler
            // 这里拿到的mCallback是null，这里强转，所以报错了
//            val mCallback =
//                FieldUtils.readField(mH, Constants.FIELD_NAME_M_CALLBACK) as Handler.Callback
//            if (mCallback !is MarsHandlerCallbackV2) {
//                mOrigin = mCallback
//                FieldUtils.writeField(mH, Constants.FIELD_NAME_M_CALLBACK, MarsHandlerCallbackV2())
//            }
            // 正确
            FieldUtils.writeField(Handler::class.java, mH, Constants.FIELD_NAME_M_CALLBACK, MarsHandlerCallbackV2(mH))
            // 错误，内部调用mH.getClass()方法，这个返回的是class android.app.ActivityThread$H，而不是class android.os.Handler
//            FieldUtils.writeField(mH, Constants.FIELD_NAME_M_CALLBACK, MarsHandlerCallbackV2(mH))
        }
    }

    var mHandler: Handler? = null

    constructor(handler: Handler) : this() {
        mHandler = handler
    }

    /**
     * Android P，用于Activity的100-109消息都统一成159了，且是通过ClientTransactionItem实现的
     * ClientTransactionItem的子类有LaunchActivityItem、ResumeActivityItem等等。
     * 因此，
     * 1. 将msg.obj转换为ClientTransaction对象
     * 2. 获取ClientTransaction对象中的mActivityCallbacks字段，该字段是ClientTransactionItem的链表
     * 3. 拿到链表后，可以找到LaunchActivityItem
     */
    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            LAUNCH_ACTIVITY -> handleLaunchActivity(msg)
            EXECUTE_TRANSACTION  -> {
                Log.e(TAG, "执行159")
                val mActivityCallbacks = FieldUtils.readField(msg.obj, "mActivityCallbacks") as List<Any>
                val className = "android.app.servertransaction.LaunchActivityItem"
                if (mActivityCallbacks.isNotEmpty()) {
                    mActivityCallbacks.forEach {
                        if (it.javaClass.name.equals(className)) {
                            val intent = FieldUtils.readField(it, "mIntent") as Intent
                            val targetIntent = intent.getParcelableExtra<Intent>(Constants.EXTRA_TARGET_INTENT)
                                ?: return@forEach
                            intent.component = targetIntent.component
                        }
                    }
                }
            }
        }
        mHandler?.handleMessage(msg)
        return true
    }

    private fun handleLaunchActivity(msg: Message) {
        val obj = msg.obj
        val raw = FieldUtils.readField(obj, Constants.FILED_NAME_INTENT) as Intent
        val targetIntent = raw.getParcelableExtra<Intent>(Constants.EXTRA_TARGET_INTENT)
            ?: return
        // 如果Hook操作是在Application中调用的，那么targetIntent需要判空；如果放在MainActivity中执行，则无需判空
        raw.component = targetIntent.component
    }

}