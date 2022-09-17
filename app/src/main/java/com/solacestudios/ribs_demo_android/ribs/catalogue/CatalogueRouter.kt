package com.solacestudios.ribs_demo_android.ribs.catalogue

import com.solacestudios.ribs_demo_android.ribs.details.DetailsBuilder
import com.solacestudios.ribs_demo_android.ribs.details.DetailsRouter

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link CatalogueBuilder.CatalogueScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class CatalogueRouter(
    view: CatalogueView,
    interactor: CatalogueInteractor,
    component: CatalogueBuilder.Component,
    private val detailsBuilder: DetailsBuilder
) : ViewRouter<CatalogueView, CatalogueInteractor>(view, interactor, component) {

    private var detailsRouter: DetailsRouter? = null

    fun attachDetails() {
        detailsRouter = detailsBuilder.build(view)
        detailsRouter?.let {
            attachChild(it)
            view.addView(it.view)
        }
    }

    fun detachDetails() {
        detailsRouter?.let {
            detachChild(it)
            view.removeView(it.view)
        }
    }
}
