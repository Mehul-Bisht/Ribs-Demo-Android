package com.solacestudios.ribs_demo_android.ribs.home

import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.network.CatalogueService
import com.solacestudios.ribs_demo_android.repository.CatalogueRepository
import com.solacestudios.ribs_demo_android.repository.CatalogueRepositoryImpl
import com.solacestudios.ribs_demo_android.ribs.catalogue.*
import com.solacestudios.ribs_demo_android.util.Constants
import com.solacestudios.ribs_demo_android.util.RibsScheduler
import com.uber.rib.core.InteractorBaseComponent
import dagger.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

@HomeScope
@Component(
    modules = [HomeModule::class],
    dependencies = [HomeComponent.ParentComponent::class]
)
interface HomeComponent : InteractorBaseComponent<HomeInteractor>, CatalogueComponent.ParentComponent {
    fun getHomeRouter(): HomeRouter

    interface ParentComponent {
        fun homeListener(): HomeInteractor.Listener
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: HomeInteractor): Builder

        @BindsInstance
        fun view(view: HomeView): Builder

        fun parentComponent(component: ParentComponent): Builder

        fun build(): HomeComponent
    }
}

@Module
abstract class HomeModule {
    @Binds
    @HomeScope
    internal abstract fun buildPresenter(view: HomeView): HomeInteractor.Presenter

    @Binds
    @HomeScope
    internal abstract fun buildContainerView(view: HomeView): ViewGroup

    @Module
    companion object {

        var logging : HttpLoggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
        var clientHome : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        @HomeScope
        @Provides
        @JvmStatic
        internal fun router(view: HomeView, component: HomeComponent, interactor: HomeInteractor): HomeRouter =
            HomeRouter(view, interactor, component, CatalogueBuilder(component))

        @HomeScope
        @Provides
        @JvmStatic
        internal fun provideCatalogueParentListener(interactor: HomeInteractor): CatalogueInteractor.Listener =
            interactor.CatalogueParentListener()

        @HomeScope
        @Provides
        @JvmStatic
        internal fun provideRetrofit(
            gsonConverterFactory: GsonConverterFactory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(clientHome)
                .build()
        }

        @HomeScope
        @Provides
        @JvmStatic
        internal fun provideService(
            retrofit: Retrofit,
        ): CatalogueService {
            return retrofit.create(CatalogueService::class.java)
        }

        @HomeScope
        @Provides
        @JvmStatic
        internal fun provideCatalogueRepository(
            service: CatalogueService
        ): CatalogueRepository {
            return CatalogueRepositoryImpl(service)
        }

        @Provides
        fun providesGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }
    }
}

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class HomeScope
