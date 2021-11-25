package com.mars.plugin.tool;

import com.mars.toolapi.IUploadTask;

/**
 * Created by JohnnySwordMan on 2021/11/24
 *
 * 遇到的问题：在宿主中通过反射的方式获取UploadTask的class，然后调用newInstance方法报错。
 * https://blog.csdn.net/AriesTina/article/details/100516127
 */
public class UploadTask implements IUploadTask {

    private String taskName = "upload-task";

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
