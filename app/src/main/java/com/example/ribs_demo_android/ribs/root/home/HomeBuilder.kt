package com.example.ribs_demo_android.ribs.root.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.repository.CatalogueRepositoryImpl
import com.example.ribs_demo_android.ribs.root.category.CategoryInteractor
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueBuilder
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueInteractor
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueScheduler
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueSchedulerImpl
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.util.Constants
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link LogoutScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class HomeBuilder(dependency: ParentComponent) :
    ViewBuilder<HomeView, HomeRouter, HomeBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [LogoutRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [LogoutRouter].
     */
    fun build(parentViewGroup: ViewGroup): HomeRouter {
        val view = createView(parentViewGroup)
        val interactor = HomeInteractor()
        val component = DaggerHomeBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.logoutRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): HomeView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.home_rib, parentViewGroup, false) as HomeView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
        //fun logoutListener(): LogoutInteractor.LogoutListener
        fun homeToggleListener(): HomeInteractor.HomeToggleListener
    }

    @dagger.Module
    abstract class Module {

        @HomeScope
        @Binds
        internal abstract fun presenter(view: HomeView): HomeInteractor.HomePresenter

        @dagger.Module
        companion object {

            var logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY)
            var clientHome : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

            @HomeScope
            @Provides
            @JvmStatic
            internal fun provideCatalogueListener(
                interactor: HomeInteractor
            ): CatalogueInteractor.CatalogueListener {
                return interactor.HomeListener()
            }

            @HomeScope
            @Provides
            @JvmStatic
            internal fun provideRetrofit(
                gsonConverterFactory: GsonConverterFactory
            ): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(gsonConverterFactory)
                    .client(clientHome)
                    .build()
            }

            @HomeScope
            @Provides
            @JvmStatic
            internal fun provideService(
                retrofit: Retrofit
            ): CatalogueService {
                return retrofit.create(CatalogueService::class.java)
            }

            @HomeScope
            @Provides
            @JvmStatic
            internal fun provideCatalogueRepository(
                service: CatalogueService,
                scheduler: CatalogueScheduler
            ): CatalogueRepository {
                return CatalogueRepositoryImpl(service, scheduler)
            }

            @Provides
            fun providesGsonConverterFactory(): GsonConverterFactory {
                return GsonConverterFactory.create()
            }

            @HomeScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: HomeView,
                interactor: HomeInteractor,
                service: CatalogueService
            ): HomeRouter {
                return HomeRouter(
                    view,
                    interactor,
                    component,
                    CatalogueBuilder(component)
                )
            }

            @HomeScope
            @Provides
            @JvmStatic
            internal fun provideCatalogueSchedulers(): CatalogueScheduler {
                return CatalogueSchedulerImpl()
            }
            // TODO: Create provider methods for dependencies created by this Rib. These should be static.
        }
    }

    @HomeScope
    @dagger.Component(
        modules = arrayOf(Module::class),
        dependencies = arrayOf(ParentComponent::class)
    )
    interface Component : InteractorBaseComponent<HomeInteractor>, CatalogueBuilder.ParentComponent,
        BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: HomeInteractor): Builder

            @BindsInstance
            fun view(view: HomeView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun logoutRouter(): HomeRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class HomeScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class LogoutInternal
}
