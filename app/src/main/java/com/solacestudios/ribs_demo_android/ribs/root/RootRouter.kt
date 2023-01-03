package com.solacestudios.ribs_demo_android.ribs.root

import com.solacestudios.ribs_demo_android.ribs.category.CategoryBuilder
import com.solacestudios.ribs_demo_android.ribs.category.CategoryRouter
import com.solacestudios.ribs_demo_android.ribs.home.HomeBuilder
import com.solacestudios.ribs_demo_android.ribs.home.HomeRouter
import com.solacestudios.ribscodegen.root.RootComponent

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: RootComponent,
    private val categoryBuilder: CategoryBuilder,
    private val homeBuilder: HomeBuilder
) : ViewRouter<RootView, RootInteractor>(view, interactor, component) {
    private var categoryRouter: CategoryRouter? = null
    private var homeRouter: HomeRouter? = null

    fun attachHome() {
        homeRouter = homeBuilder.build(view)
        homeRouter?.let {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun attachCategory() {
        categoryRouter = categoryBuilder.build(view)
        categoryRouter?.let {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachHome() {
        homeRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
    }

    fun detachCategory() {
        categoryRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
    }
}
