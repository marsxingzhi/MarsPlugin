package com.mars.plugin.tool;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RecordActivity extends Activity {

    private static final String TAG = "RecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_record);
        Log.e(TAG, "插件Activity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "插件Activity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "插件Activity onDestroy");
    }
}