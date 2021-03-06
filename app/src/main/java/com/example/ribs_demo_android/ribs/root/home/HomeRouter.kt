package com.example.ribs_demo_android.ribs.root.home

import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueBuilder
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueRouter
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link HomeBuilder.HomeScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class HomeRouter(
    view: HomeView,
    interactor: HomeInteractor,
    component: HomeBuilder.Component,
    private val catalogueBuilder: CatalogueBuilder
) : ViewRouter<HomeView, HomeInteractor>(view, interactor, component) {

    private var catalogueRouter: CatalogueRouter? = null

    fun attachCatalogue() {
        catalogueRouter = catalogueBuilder.build(view)
        catalogueRouter?.let {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachCatalogue() {
        catalogueRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
    }
}
