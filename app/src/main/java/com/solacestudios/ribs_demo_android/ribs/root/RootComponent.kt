package com.solacestudios.ribscodegen.root

import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.ribs.root.RootInteractor
import com.solacestudios.ribs_demo_android.ribs.root.RootRouter
import com.solacestudios.ribs_demo_android.ribs.root.RootView
import com.solacestudios.ribs_demo_android.ribs.category.CategoryBuilder
import com.solacestudios.ribs_demo_android.ribs.category.CategoryComponent
import com.solacestudios.ribs_demo_android.ribs.category.CategoryInteractor
import com.solacestudios.ribs_demo_android.ribs.home.HomeBuilder
import com.solacestudios.ribs_demo_android.ribs.home.HomeComponent
import com.solacestudios.ribs_demo_android.ribs.home.HomeInteractor
import com.uber.rib.core.InteractorBaseComponent
import dagger.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

@RootScope
@Component(
    modules = [RootModule::class],
    dependencies = [RootComponent.ParentComponent::class]
)
interface RootComponent : InteractorBaseComponent<RootInteractor>, CategoryComponent.ParentComponent, HomeComponent.ParentComponent {
    fun getRootRouter(): RootRouter

    interface ParentComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: RootInteractor): Builder

        @BindsInstance
        fun view(view: RootView): Builder

        fun parentComponent(component: ParentComponent): Builder

        fun build(): RootComponent
    }
}

@Module
abstract class RootModule {
    @Binds
    @RootScope
    internal abstract fun buildPresenter(view: RootView): RootInteractor.Presenter

    @Binds
    @RootScope
    internal abstract fun buildContainerView(view: RootView): ViewGroup

    @Module
    companion object {
        @RootScope
        @Provides
        @JvmStatic
        internal fun router(view: RootView, component: RootComponent, interactor: RootInteractor): RootRouter =
            RootRouter(view, interactor, component, CategoryBuilder(component), HomeBuilder(component))

        @RootScope
        @Provides
        @JvmStatic
        internal fun provideCategoryParentListener(interactor: RootInteractor): CategoryInteractor.Listener =
            interactor.CategoryParentListener()

        @RootScope
        @Provides
        @JvmStatic
        internal fun provideHomeParentListener(interactor: RootInteractor): HomeInteractor.Listener =
            interactor.HomeParentListener()
    }
}

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class RootScope
