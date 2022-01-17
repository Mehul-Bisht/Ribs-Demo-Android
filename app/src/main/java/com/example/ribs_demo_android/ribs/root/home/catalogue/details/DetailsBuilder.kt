package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.ribs.root.home.catalogue.DataStream
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link DetailsScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class DetailsBuilder(dependency: ParentComponent) :
    ViewBuilder<DetailsView, DetailsRouter, DetailsBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [DetailsRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DetailsRouter].
     */
    fun build(parentViewGroup: ViewGroup): DetailsRouter {
        val view = createView(parentViewGroup)
        val interactor = DetailsInteractor()
        val component = DaggerDetailsBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.detailsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DetailsView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.details_rib, parentViewGroup, false) as DetailsView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
        fun detailsListener(): DetailsInteractor.DetailsListener
        fun service(): DetailsService
        fun dataSteam(): DataStream
        fun detailsRepository(): DetailsRepository
        fun detailsScheduler(): DetailsScheduler
    }

    @dagger.Module
    abstract class Module {

        @DetailsScope
        @Binds
        internal abstract fun presenter(view: DetailsView): DetailsInteractor.DetailsPresenter

        @dagger.Module
        companion object {

            @DetailsScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: DetailsView,
                interactor: DetailsInteractor
            ): DetailsRouter {
                return DetailsRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @DetailsScope
    @dagger.Component(
        modules = arrayOf(Module::class),
        dependencies = arrayOf(ParentComponent::class)
    )
    interface Component : InteractorBaseComponent<DetailsInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DetailsInteractor): Builder

            @BindsInstance
            fun view(view: DetailsView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun detailsRouter(): DetailsRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class DetailsScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class DetailsInternal
}
