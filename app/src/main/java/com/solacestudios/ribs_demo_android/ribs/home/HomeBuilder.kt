package com.solacestudios.ribs_demo_android.ribs.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.R
import com.uber.rib.core.ViewBuilder

/**
 * Builder for the {@link LogoutScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class HomeBuilder(dependency: HomeComponent.ParentComponent) :
    ViewBuilder<HomeView, HomeRouter, HomeComponent.ParentComponent>(dependency) {

    /**
     * Builds a new [LogoutRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [LogoutRouter].
     */
    fun build(parentViewGroup: ViewGroup): HomeRouter {
        val view = createView(parentViewGroup)
        val interactor = HomeInteractor()
        val component = DaggerHomeComponent.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.getHomeRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): HomeView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.home_rib, parentViewGroup, false) as HomeView
    }
}
