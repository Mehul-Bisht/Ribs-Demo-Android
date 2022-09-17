package com.solacestudios.ribs_demo_android.ribs.catalogue

import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.network.CatalogueService
import com.solacestudios.ribs_demo_android.network.DetailsService
import com.solacestudios.ribs_demo_android.repository.CatalogueRepository
import com.solacestudios.ribs_demo_android.repository.DetailsRepository
import com.solacestudios.ribs_demo_android.repository.DetailsRepositoryImpl
import com.solacestudios.ribs_demo_android.ribs.details.DetailsBuilder
import com.solacestudios.ribs_demo_android.ribs.details.DetailsInteractor
import com.solacestudios.ribs_demo_android.ribs.details.DetailsScheduler
import com.solacestudios.ribs_demo_android.ribs.details.DetailsSchedulerImpl
import com.solacestudios.ribs_demo_android.util.Constants
import com.solacestudios.ribs_demo_android.ribs.details.DetailsComponent
import com.solacestudios.ribs_demo_android.util.DataStream
import com.solacestudios.ribs_demo_android.util.MutableDataStream
import com.uber.rib.core.InteractorBaseComponent
import dagger.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope

@CatalogueScope
@Component(
    modules = [CatalogueModule::class],
    dependencies = [CatalogueComponent.ParentComponent::class]
)
interface CatalogueComponent : InteractorBaseComponent<CatalogueInteractor>, DetailsComponent.ParentComponent {
    fun getCatalogueRouter(): CatalogueRouter

    interface ParentComponent {
        fun service(): CatalogueService
        fun catalogueListener(): CatalogueInteractor.Listener
        fun catalogueRepository(): CatalogueRepository
        fun catalogueSchedulers(): CatalogueScheduler
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: CatalogueInteractor): Builder

        @BindsInstance
        fun view(view: CatalogueView): Builder

        fun parentComponent(component: ParentComponent): Builder

        fun build(): CatalogueComponent
    }
}

@Module
abstract class CatalogueModule {
    @Binds
    @CatalogueScope
    internal abstract fun buildPresenter(view: CatalogueView): CatalogueInteractor.Presenter

    @Binds
    @CatalogueScope
    internal abstract fun buildContainerView(view: CatalogueView): ViewGroup

    @Module
    companion object {
        var logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY)
        var clientCatalogue : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        @CatalogueScope
        @Provides
        @JvmStatic
        internal fun router(view: CatalogueView, component: CatalogueComponent, interactor: CatalogueInteractor): CatalogueRouter =
            CatalogueRouter(view, interactor, component, DetailsBuilder(component))

        @CatalogueScope
        @Provides
        @JvmStatic
        internal fun provideDetailsParentListener(interactor: CatalogueInteractor): DetailsInteractor.Listener =
            interactor.DetailsParentListener()

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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
}

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class CatalogueScope

@Qualifier
@Retention(RetentionPolicy.CLASS)
internal annotation class CatalogueInternal