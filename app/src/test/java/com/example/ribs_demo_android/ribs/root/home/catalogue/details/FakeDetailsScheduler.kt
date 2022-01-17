package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class FakeDetailsScheduler: DetailsScheduler {

    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val main: Scheduler
        get() = Schedulers.trampoline()
    override val compute: Scheduler
        get() = Schedulers.trampoline()
}