package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface DetailsScheduler {
    val io: Scheduler
    val main: Scheduler
    val compute: Scheduler
}

class DetailsSchedulerImpl(): DetailsScheduler {
    override val io: Scheduler
        get() = Schedulers.io()
    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val compute: Scheduler
        get() = Schedulers.computation()
}