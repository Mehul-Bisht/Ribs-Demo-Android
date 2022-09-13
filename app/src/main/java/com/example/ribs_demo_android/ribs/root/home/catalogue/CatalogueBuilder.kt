package com.example.ribs_demo_android.ribs.root.home.catalogue

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.network.repository.DetailsRepositoryImpl
import com.example.ribs_demo_android.ribs.root.home.HomeBuilder
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsBuilder
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsInteractor
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsScheduler
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsSchedulerImpl
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
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
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link CatalogueScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class CatalogueBuilder(dependency: ParentComponent) :
    ViewBuilder<CatalogueView, CatalogueRouter, CatalogueBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [CatalogueRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [CatalogueRouter].
     */
    fun build(parentViewGroup: ViewGroup): CatalogueRouter {
        val view = createView(parentViewGroup)
        val interactor = CatalogueInteractor()
        val component = DaggerCatalogueBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.catalogueRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CatalogueView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.catalogue_rib, parentViewGroup, false) as CatalogueView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
        fun service(): CatalogueService
        fun catalogueListener(): CatalogueInteractor.CatalogueListener
        fun catalogueRepository(): CatalogueRepository
        fun catalogueSchedulers(): CatalogueScheduler
    }

    @dagger.Module
    abstract class Module {

        @CatalogueScope
        @Binds
        internal abstract fun presenter(view: CatalogueView): CatalogueInteractor.CataloguePresenter

        @dagger.Module
        companion object {

            var logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
                HttpLoggingInterceptor.Level.BODY)
            var clientCatalogue : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

            @CatalogueScope
            @Provides
            @JvmStatic
            internal fun provideDetailsListener(
                interactor: CatalogueInteractor
            ): DetailsInteractor.DetailsListener {
                return interactor.DetailsListener()
            }

            @CatalogueScope
            @CatalogueInternal
            @Provides
            internal fun provideMutableDataStream(): MutableDataStream {
                return MutableDataStream("initial")
            }

            @CatalogueScope
            @Provides
            fun provideDataStream(@CatalogueInternal mutableDataStream: MutableDataStream): DataStream {
                return mutableDataStream
            }

            @CatalogueScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: CatalogueView,
                interactor: CatalogueInteractor
            ): CatalogueRouter {
                return CatalogueRouter(
                    view,
                    interactor,
                    component,
                    DetailsBuilder(component)
                )
            }

            @Provides
            @Named("gson")
            fun providesGsonConverterFactory(): GsonConverterFactory {
                return GsonConverterFactory.create()
            }

            @CatalogueScope
            @Provides
            @JvmStatic
            @Named("catalogue_retrofit")
            internal fun provideRetrofit(
                @Named("gson") gsonConverterFactory: GsonConverterFactory
            ): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(gsonConverterFactory)
                    .client(clientCatalogue)
                    .build()
            }

            @CatalogueScope
            @Provides
            @JvmStatic
            internal fun provideService(
                @Named("catalogue_retrofit") retrofit: Retrofit
            ): DetailsService {
                return retrofit.create(DetailsService::class.java)
            }

            @CatalogueScope
            @Provides
            @JvmStatic
            internal fun provideDetailsRepository(
                service: DetailsService,
                scheduler: DetailsScheduler
            ): DetailsRepository {
                return DetailsRepositoryImpl(service, scheduler)
            }

            @CatalogueScope
            @Provides
            @JvmStatic
            internal fun provideDetailsScheduler(): DetailsScheduler {
                return DetailsSchedulerImpl()
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @CatalogueScope
    @dagger.Component(
        modules = arrayOf(Module::class),
        dependencies = arrayOf(ParentComponent::class)
    )
    interface Component : InteractorBaseComponent<CatalogueInteractor>, DetailsBuilder.ParentComponent, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: CatalogueInteractor): Builder

            @BindsInstance
            fun view(view: CatalogueView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun catalogueRouter(): CatalogueRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class CatalogueScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class CatalogueInternal
}
