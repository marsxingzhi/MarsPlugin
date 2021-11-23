package com.mars.framework.plugin

import android.content.Context
import android.util.Log
import java.io.Closeable
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by JohnnySwordMan on 2021/11/23
 * 插件下载
 */
object DownloadApkHelper {

    private const val TAG = "DownloadApkHelper"

    /**
     * 模拟插件下载过程
     * assets目录下的文件使用AssetManager.open打开
     */
    fun mockDownloadPluginApk(context: Context, fileName: String) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = context.assets.open(fileName)
            // 写到data/data/packageName/files目录下
            val destFilePath = context.getFileStreamPath(fileName)
            Log.e(TAG, "下载插件并保存，文件路径：$destFilePath")
            outputStream = FileOutputStream(destFilePath)
            val buffer = ByteArray(1024)
            var count = 0
            while (inputStream.read(buffer).also { count = it } > 0) {
                outputStream.write(buffer, 0, count)
            }
            outputStream.flush()
        } catch (e: Exception) {
            closeSilently(inputStream)
            closeSilently(outputStream)
        }
    }

    private fun closeSilently(closeable: Closeable?) {
        if (closeable == null) {
            return
        }
        try {
            closeable.close()
        } catch (e: Throwable) {
            // ignore
        }
    }
}

