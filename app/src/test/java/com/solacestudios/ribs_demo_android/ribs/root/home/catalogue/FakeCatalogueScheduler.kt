package com.solacestudios.ribs_demo_android.ribs.root.home.catalogue

import com.solacestudios.ribs_demo_android.ribs.catalogue.CatalogueScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class FakeCatalogueScheduler: CatalogueScheduler {

    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val main: Scheduler
        get() = Schedulers.trampoline()
    override val compute: Scheduler
        get() = Schedulers.trampoline()
}