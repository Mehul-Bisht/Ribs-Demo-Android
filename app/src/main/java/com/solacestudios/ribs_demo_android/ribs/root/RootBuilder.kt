package com.solacestudios.ribs_demo_android.ribs.root

import android.view.LayoutInflater
import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.R
import com.solacestudios.ribscodegen.root.DaggerRootComponent
import com.solacestudios.ribscodegen.root.RootComponent
import com.uber.rib.core.ViewBuilder

/**
 * Builder for the {@link RootScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class RootBuilder(dependency: RootComponent.ParentComponent) :
    ViewBuilder<RootView, RootRouter, RootComponent.ParentComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = DaggerRootComponent.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.getRootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.root_rib, parentViewGroup, false) as RootView
    }

}
