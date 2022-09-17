package com.solacestudios.ribs_demo_android.ribs.details

import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.network.DetailsService
import com.solacestudios.ribs_demo_android.repository.DetailsRepository
import com.solacestudios.ribs_demo_android.util.DataStream
import com.uber.rib.core.InteractorBaseComponent
import dagger.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

@DetailsScope
@Component(
    modules = [DetailsModule::class],
    dependencies = [DetailsComponent.ParentComponent::class]
)
interface DetailsComponent : InteractorBaseComponent<DetailsInteractor> {
    fun getDetailsRouter(): DetailsRouter

    interface ParentComponent {
        fun detailsListener(): DetailsInteractor.Listener
        fun service(): DetailsService
        fun dataSteam(): DataStream
        fun detailsRepository(): DetailsRepository
        fun detailsScheduler(): DetailsScheduler
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: DetailsInteractor): Builder

        @BindsInstance
        fun view(view: DetailsView): Builder

        fun parentComponent(component: ParentComponent): Builder

        fun build(): DetailsComponent
    }
}

@Module
abstract class DetailsModule {
    @Binds
    @DetailsScope
    internal abstract fun buildPresenter(view: DetailsView): DetailsInteractor.Presenter

    @Binds
    @DetailsScope
    internal abstract fun buildContainerView(view: DetailsView): ViewGroup

    @Module
    companion object {
        @DetailsScope
        @Provides
        @JvmStatic
        internal fun router(view: DetailsView, component: DetailsComponent, interactor: DetailsInteractor): DetailsRouter =
            DetailsRouter(view, interactor, component)
    }
}

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class DetailsScope
