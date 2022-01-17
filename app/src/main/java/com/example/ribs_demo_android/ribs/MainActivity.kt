package com.example.ribs_demo_android.ribs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.example.ribs_demo_android.ribs.root.RootBuilder
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter

class MainActivity : RibActivity() {

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *> {
        val rootBuilder: RootBuilder = RootBuilder(object : RootBuilder.ParentComponent{})
        return rootBuilder.build(parentViewGroup)
    }
}