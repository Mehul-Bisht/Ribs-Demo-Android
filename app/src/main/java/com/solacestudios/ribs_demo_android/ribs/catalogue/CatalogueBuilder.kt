package com.solacestudios.ribs_demo_android.ribs.catalogue

import android.view.LayoutInflater
import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.R
import com.uber.rib.core.ViewBuilder

/**
 * Builder for the {@link CatalogueScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class CatalogueBuilder(dependency: CatalogueComponent.ParentComponent) :
    ViewBuilder<CatalogueView, CatalogueRouter, CatalogueComponent.ParentComponent>(dependency) {

    /**
     * Builds a new [CatalogueRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [CatalogueRouter].
     */
    fun build(parentViewGroup: ViewGroup): CatalogueRouter {
        val view = createView(parentViewGroup)
        view.initViewIds()
        val interactor = CatalogueInteractor()
        val component = DaggerCatalogueComponent.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.getCatalogueRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CatalogueView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.catalogue_rib, parentViewGroup, false) as CatalogueView
    }

}
