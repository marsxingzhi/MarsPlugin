package com.mars.framework.plugin.classloader

import android.app.Application
import android.content.Context
import com.mars.framework.plugin.helper.DownloadApkHelper
import com.mars.framework.plugin.reflect.FieldUtils
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader

/**
 * Created by JohnnySwordMan on 2021/11/24
 * Mars插件框架的ClassLoader，其parent ClassLoader是宿主的ClassLoader
 *
 * 内部维护一个ClassLoader的链表，即List<ClassLoader>，
 * MarsClassLoader的loadClass方法，会先尝试使用宿主的ClassLoader去加载类，
 * 如果不能加载，就遍历链表，直到找到一个可以加载该类的ClassLoader
 *
 * 注意：MarsClassLoader与插件ClassLoader没有上下级的关系，它们的parent ClassLoader都是宿主ClassLoader
 */
class MarsClassLoader(dexPath: String, parent: ClassLoader) : PathClassLoader(dexPath, parent) {


    companion object {

        /**
         * 从这里可以看出MarsClassLoader和插件的ClassLoader并没有上下级的关系，它们的parent ClassLoader都是宿主ClassLoader
         */
        fun onHook(app: Application?) {
            app ?: return
            val mBaseContext = app.baseContext
            // TODO 关注这个
            // ContextImpl中的LoadedApk对象中的mPackageInfo，传说中的命中缓存！
            val mPackageInfo = FieldUtils.readField(mBaseContext, "mPackageInfo")
            val marsClassLoader =
                MarsClassLoader(mBaseContext.packageCodePath, mBaseContext.classLoader)

            // 插件ClassLoader
            val dexOutputFile = mBaseContext.getDir("dex", Context.MODE_PRIVATE)

            val assetsList = app.assets.list("")
            assetsList?.forEach {
                if (it.endsWith(".apk")) {
                    DownloadApkHelper.extractAssets(app, it)
                    val pluginFile = mBaseContext.getFileStreamPath(it)
                    val pluginDexClassLoader = DexClassLoader(
                        pluginFile.absolutePath,
                        dexOutputFile.absolutePath,
                        null,
                        mBaseContext.classLoader
                    )
                    marsClassLoader.addPluginClassLoader(pluginDexClassLoader)
                }
            }

            FieldUtils.writeField(mPackageInfo, "mClassLoader", marsClassLoader)
            // TODO 为啥要设置这个
            Thread.currentThread().contextClassLoader = marsClassLoader
        }
    }

    private val mClassLoaderList = arrayListOf<ClassLoader>()

    /**
     * 添加插件的ClassLoader
     */
    fun addPluginClassLoader(dexClassLoader: DexClassLoader?) {
        dexClassLoader?.let {
            mClassLoaderList.add(it)
        }
    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        var clazz: Class<*>? = null
        try {
            clazz = parent.loadClass(name)
        } catch (e: ClassNotFoundException) {

        }
        if (clazz != null) {
            return clazz
        }
        // 如果为空，则从链表中查找
        mClassLoaderList.forEach {
            try {
                clazz = it.loadClass(name)
                if (clazz != null) {
                    return clazz!!
                }
            } catch (e: ClassNotFoundException) {

            }
        }
        throw ClassNotFoundException("无法加载 $name")
    }

}