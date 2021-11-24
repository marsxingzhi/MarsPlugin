package com.mars.framework.plugin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {

    private lateinit var mBtnStartPlugin: Button
    private lateinit var mBtnInvokePluginMethod: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtnStartPlugin = findViewById(R.id.btn_start_plugin)
        mBtnInvokePluginMethod = findViewById(R.id.btn_invoke_plugin)

        mBtnStartPlugin.setOnClickListener {
            val intent = Intent(this, PluginTestActivity::class.java)
            startActivity(intent)
        }
        mBtnInvokePluginMethod.setOnClickListener {
            val uploadTaskCls = classLoader.loadClass("com.mars.plugin.tool.UploadTask")
            val uploadTask = uploadTaskCls.newInstance() 
        }
    }
}