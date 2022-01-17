package com.example.ribs_demo_android.ribs.root

import com.example.ribs_demo_android.ribs.root.category.CategoryBuilder
import com.example.ribs_demo_android.ribs.root.category.CategoryRouter
import com.example.ribs_demo_android.ribs.root.home.HomeBuilder
import com.example.ribs_demo_android.ribs.root.home.HomeRouter

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: RootBuilder.Component,
    private val homeBuilder: HomeBuilder,
    private val categoryBuilder: CategoryBuilder
) : ViewRouter<RootView, RootInteractor>(view, interactor, component) {

    private var homeRouter: HomeRouter? = null
    private var categoryRouter: CategoryRouter? = null

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
