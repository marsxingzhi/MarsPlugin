package com.mars.framework.plugin.hook

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.mars.framework.plugin.Constants
import com.mars.framework.plugin.ext.log
import com.mars.framework.plugin.helper.PluginHelper
import com.mars.framework.plugin.stub.StubStandardActivity0
import com.mars.framework.plugin.utils.FieldUtil
import com.mars.framework.plugin.utils.MethodUtil

/**
 * Created by JohnnySwordMan on 2021/11/11
 */
class MarsInstrumentation() : Instrumentation() {

    companion object {
        const val TAG = "MarsInstrumentation"

        /**
         * 替换ActivityThread的mInstrumentation
         */
        fun onHook() {
            val currentActivityThread = PluginHelper.getCurrentActivityThread()
            val mInstrumentationField =
                FieldUtil.readField(currentActivityThread, "mInstrumentation")
            if (mInstrumentationField !is MarsInstrumentation) {
//                mOrigin = mInstrumentationField as Instrumentation
                FieldUtil.writeField(
                    "mInstrumentation",
                    currentActivityThread,
                    MarsInstrumentation(mInstrumentationField as Instrumentation)
                )
                log(TAG, "hook success")
            }
        }
    }

    var mOrigin: Instrumentation? = null

    constructor(instrumentation: Instrumentation) : this() {
        mOrigin = instrumentation
    }

    /**
     * 报错：Uninitialized ActivityThread, likely app-created Instrumentation, disabling AppComponentFactory java.lang.Throwable
     *
     * Android 9.0及其以上版本，修改了newActivity，内部会判断mThread字段是否为空，mThread即ActivityThread对象
     * 自定义的MarsInstrumentation对象中没有给mThread赋值
     *
     * 这里有两种修改方式：
     * 方案一：区分版本，低于9.0的，super.newActivity；大于等于9.0的，可以自己loadClass，反正在大于等于9.0版本，newActivity的作用只是loadClass
     * 方案二：直接调用mOrigin的newActivity方法
     *
     * 对于方案二，一开始我也有疑问，mOrigin的newActivity方法在高版本不也是会判断mThread是否为空吗？
     * 其实，mThread的赋值是在Instrumentation的init方法和basicInit方法中，而这个方法是在ActivityThread#handleBindApplication中执行的，
     * 那么既然我们是hook Instrumentation，那么说明此时已经创建了Instrumentation对象，
     * 只是将ActivityThread中的mInstrumentation字段通过hook方式指向我们自定义的MarsInstrumentation。
     * 在我们开始Activity跳转的时候，bindApplication已经执行了，
     * 因此原先的Instrumentation对象执行创建且已经执行了init或basicInit方法，即mThread一定不为空
     */
    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
//        if (OSUtil.isAndroidPHigher()) {
//            // 自己loadClass
//        } else {
//            return super.newActivity(cl, className, intent)
//        }
        return mOrigin!!.newActivity(cl, className, intent)
    }

    fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder?, target: Activity?,
        intent: Intent?, requestCode: Int, options: Bundle?
    ): ActivityResult? {
        log(TAG, "hook execStartActivity")
        val paramTypes = arrayOf(
            Context::class.java,
            IBinder::class.java,
            IBinder::class.java,
            Activity::class.java,
            Intent::class.java,
            Int::class.java,
            Bundle::class.java
        )
        val paramValues =
            arrayOf(who, contextThread, token, target, wrapIntent(intent), requestCode, options)
        return MethodUtil.invokeMethod(
            mOrigin,
            "execStartActivity",
            paramTypes,
            paramValues
        ) as ActivityResult?
    }

    /**
     * 替换原始的Intent
     * TODO@geyan，这里还需要考虑启动模式，需要找到对应的Stub
     */
    private fun wrapIntent(intent: Intent?): Intent {
        val newIntent = Intent()
        // 包名是com.mars.framework.plugin，不是com.mars.framework.plugin.stub
        val stubPackage = Constants.STUB_PACKAGE_NAME
        // 将要启动的Activity替换成StubActivity
        val componentName = ComponentName(stubPackage, StubStandardActivity0::class.java.name)
        newIntent.component = componentName
        newIntent.putExtra(Constants.EXTRA_TARGET_INTENT, intent)
        return newIntent
    }

}