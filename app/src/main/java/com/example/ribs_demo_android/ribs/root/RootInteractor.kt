package com.example.ribs_demo_android.ribs.root

import android.util.Log
import com.example.ribs_demo_android.ribs.root.category.CategoryInteractor
import com.example.ribs_demo_android.ribs.root.home.HomeInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject
    lateinit var presenter: RootPresenter

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
    interface RootPresenter

    inner class RootListener: HomeInteractor.HomeToggleListener {
        override fun toggleHome() {
            router.detachHome()
            router.attachCategory()
        }
    }

    inner class RootCategoryListener: CategoryInteractor.CategoryToggleListener {
        override fun toggleCategory() {
            router.detachCategory()
            router.attachHome()
        }
    }
}
