package com.solacestudios.ribs_demo_android.ribs.root

import com.solacestudios.ribs_demo_android.ribs.category.CategoryInteractor
import com.solacestudios.ribs_demo_android.ribs.home.HomeInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.Presenter
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.Presenter, RootRouter>() {

    @Inject
    lateinit var buildPresenter: Presenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
        router.attachHome()
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface Presenter

    inner class HomeParentListener : HomeInteractor.Listener {
        override fun toggleHome() {
            router.detachHome()
            router.attachCategory()
        }
    }
    inner class CategoryParentListener : CategoryInteractor.Listener {
        override fun toggleCategory() {
            router.detachCategory()
            router.attachHome()
        }
    }

}
