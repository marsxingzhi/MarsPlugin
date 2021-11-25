package com.mars.framework.plugin

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mars.toolapi.IUploadTask

class MainActivity : Activity() {

    private lateinit var mBtnStartPlugin: Button
    private lateinit var mBtnInvokePluginMethod: Button
    private lateinit var mBtnStartRecord: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnStartPlugin = findViewById(R.id.btn_start_plugin)
        mBtnInvokePluginMethod = findViewById(R.id.btn_invoke_plugin)
        mBtnStartRecord = findViewById(R.id.btn_start_record)

        mBtnStartPlugin.setOnClickListener {
            val intent = Intent(this, PluginTestActivity::class.java)
            startActivity(intent)
        }
        mBtnInvokePluginMethod.setOnClickListener {
            // 面向接口编程，这里只能拿到Object对象，想要调用UploadTask中的方法，只能通过反射
            // 这个getClassLoader，调用的是ContextImpl的getClassLoader，内部是获取的是LoadedApk的mClassLoader
            val uploadTaskCls = classLoader.loadClass("com.mars.plugin.tool.UploadTask")
            val uploadTask = uploadTaskCls.newInstance() as IUploadTask
            Log.e("gy", "uploadTask name = ${uploadTask.taskName}")
        }

        mBtnStartRecord.setOnClickListener {
//            val intent = Intent(this, Record::class.java)
//            startActivity(intent)
            val intent = Intent()
            // demo就这样写，但是我感觉最好是调用插件中的方法，在插件中自己start Activity
            val pluginPackageName = "com.mars.plugin.tool"
            val activityName = "$pluginPackageName.RecordActivity"
            intent.component = ComponentName(pluginPackageName, activityName)
            startActivity(intent)
        }
    }
}