package com.example.ribs_demo_android.ribs

import android.util.Log
import android.view.ViewGroup
import com.example.ribs_demo_android.ribs.root.RootBuilder
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

class MainActivity : RibActivity() {

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *> {
        val rootBuilder: RootBuilder = RootBuilder(object : RootBuilder.ParentComponent{})

        RxJavaPlugins.setErrorHandler { e: Throwable ->
            var x = e
            if (x is UndeliverableException) {
                //x = e.cause!!
                return@setErrorHandler
            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
            Log.println(Log.WARN,"Undeliverable exception received, not sure what to do", x.message!!)
        }

        return rootBuilder.build(parentViewGroup)
    }
}