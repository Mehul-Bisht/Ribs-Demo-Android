package com.solacestudios.ribs_demo_android.ribs.details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.R
import com.uber.rib.core.ViewBuilder

/**
 * Builder for the {@link DetailsScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class DetailsBuilder(dependency: DetailsComponent.ParentComponent) :
    ViewBuilder<DetailsView, DetailsRouter, DetailsComponent.ParentComponent>(dependency) {

    /**
     * Builds a new [DetailsRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DetailsRouter].
     */
    fun build(parentViewGroup: ViewGroup): DetailsRouter {
        val view = createView(parentViewGroup)
        view.initViewIds()
        val interactor = DetailsInteractor()
        val component = DaggerDetailsComponent.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.getDetailsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DetailsView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.details_rib, parentViewGroup, false) as DetailsView
    }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
}


