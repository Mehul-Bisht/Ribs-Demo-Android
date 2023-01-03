package com.solacestudios.ribs_demo_android.ribs.root.category

import com.solacestudios.ribs_demo_android.ribs.category.CategoryScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class FakeCategoryScheduler: CategoryScheduler {

    override val io: Scheduler
        get() = Schedulers.trampoline()
    override val main: Scheduler
        get() = Schedulers.trampoline()
    override val compute: Scheduler
        get() = Schedulers.trampoline()
}