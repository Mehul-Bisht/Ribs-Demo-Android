package com.solacestudios.ribs_demo_android.ribs.catalogue

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


interface CatalogueScheduler {
    val io: Scheduler
    val main: Scheduler
    val compute: Scheduler
}

class CatalogueSchedulerImpl(): CatalogueScheduler {
    override val io: Scheduler
        get() = Schedulers.io()
    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val compute: Scheduler
        get() = Schedulers.computation()
}