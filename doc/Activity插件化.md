# 概述

Activity插件化主要需要解决以下几个问题：

1. 宿主如何调起没有在manifest文件中声明的Activity
2. 宿主如何调起没有在manifest文件中声明的**插件**Activity
3. 需要考虑Android系统版本的兼容性。例如：>=8.0，废弃了AMN盒AMP



# 如何启动没有在manifest文件中声明的Activity

主要思想就是**欺上瞒下**

1. hook上半场：在发送给AMS之前，将要启动的Activity替换成已经在manifest中声明的占位Activity
2. hook下半场：在发送给ActivityThread的时候，将占位Activity还原成要启动的Activity



方案：

1. 首先采用占位Activity的思想
2. hook Instrumentation，而不是AMN，因为在Android8.0及其以上，AMN和AMP废弃了，如果hook AMN的话，需要考虑兼容问题
3. hook mH的mCallback字段



## 开发步骤

- [ ] hook Instrumentation
