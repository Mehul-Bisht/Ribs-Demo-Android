package com.solacestudios.ribs_demo_android.ribs.category

import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.network.CategoryService
import com.solacestudios.ribs_demo_android.util.Constants
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
import javax.inject.Scope

@CategoryScope
@Component(
    modules = [CategoryModule::class],
    dependencies = [CategoryComponent.ParentComponent::class]
)
interface CategoryComponent : InteractorBaseComponent<CategoryInteractor> {
    fun getCategoryRouter(): CategoryRouter

    interface ParentComponent {
        fun categoryListener(): CategoryInteractor.Listener
    }

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun interactor(interactor: CategoryInteractor): Builder

        @BindsInstance
        fun view(view: CategoryView): Builder

        fun parentComponent(component: ParentComponent): Builder

        fun build(): CategoryComponent
    }
}

@Module
abstract class CategoryModule {
    @Binds
    @CategoryScope
    internal abstract fun buildPresenter(view: CategoryView): CategoryInteractor.Presenter

    @Binds
    @CategoryScope
    internal abstract fun buildContainerView(view: CategoryView): ViewGroup

    @Module
    companion object {
        var logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY)
        var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        @CategoryScope
        @Provides
        @JvmStatic
        internal fun router(view: CategoryView, component: CategoryComponent, interactor: CategoryInteractor): CategoryRouter =
            CategoryRouter(view, interactor, component)

        @Provides
        fun providesGsonConverterFactory(): GsonConverterFactory {
            return GsonConverterFactory.create()
        }

        @CategoryScope
        @Provides
        @JvmStatic
        internal fun provideRetrofit(
            gsonConverterFactory: GsonConverterFactory
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(client)
                .build()
        }

        @CategoryScope
        @Provides
        @JvmStatic
        internal fun provideCategoryService(
            retrofit: Retrofit
        ): CategoryService {
            return retrofit.create(CategoryService::class.java)
        }

        @CategoryScope
        @Provides
        @JvmStatic
        @Named("category")
        internal fun provideCategorySchedulers(): CategoryScheduler {
            return CategorySchedulerImpl()
        }


    }
}

@Scope
@Retention(RetentionPolicy.CLASS)
internal annotation class CategoryScope
