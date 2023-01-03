package com.solacestudios.ribs_demo_android.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface RibsScheduler  {
    val io: Scheduler
    val main: Scheduler
    val compute: Scheduler
}

class RibsSchedulerImpl(): RibsScheduler {
    override val io: Scheduler
        get() = Schedulers.io()
    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val compute: Scheduler
        get() = Schedulers.computation()
}
