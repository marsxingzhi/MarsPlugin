package com.mars.framework.plugin.stub

import android.os.Bundle
import com.mars.framework.plugin.ext.log

/**
 * Created by geyan on 2021/2/12
 */

class StubStandardActivity0 : BaseStubActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("StubStandardActivity0", "onCreate")
    }

}