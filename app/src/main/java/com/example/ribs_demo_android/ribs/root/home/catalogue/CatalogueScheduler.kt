package com.example.ribs_demo_android.ribs.root.home.catalogue

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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