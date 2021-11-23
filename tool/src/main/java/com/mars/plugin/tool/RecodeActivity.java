package com.mars.plugin.tool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class RecodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_recode);
        Log.e("gy", "插件RecodeActivity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("gy", "插件RecodeActivity onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("gy", "插件RecodeActivity onDestroy");
    }
}