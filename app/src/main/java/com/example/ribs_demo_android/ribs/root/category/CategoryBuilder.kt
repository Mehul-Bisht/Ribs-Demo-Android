package com.example.ribs_demo_android.ribs.root.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.network.CategoryService
import com.example.ribs_demo_android.util.Constants
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Scope
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

/**
 * Builder for the {@link CategoryScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class CategoryBuilder(dependency: ParentComponent) :
    ViewBuilder<CategoryView, CategoryRouter, CategoryBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [CategoryRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [CategoryRouter].
     */
    fun build(parentViewGroup: ViewGroup): CategoryRouter {

        val view = createView(parentViewGroup)
        view.initViewIds()
        val interactor = CategoryInteractor()
        val component = DaggerCategoryBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.categoryRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CategoryView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.category_rib, parentViewGroup, false) as CategoryView
    }

    interface ParentComponent {
        // TODO: Define dependencies required from your parent interactor here.
        fun categoryToggleListener(): CategoryInteractor.CategoryToggleListener
    }

    @dagger.Module
    abstract class Module {

        @CategoryScope
        @Binds
        internal abstract fun presenter(view: CategoryView): CategoryInteractor.CategoryPresenter

        @dagger.Module
        companion object {

            var logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            var client : OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()


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

//            @CategoryScope
//            @Provides
//            @JvmStatic
//            @Named("categoryRepo")
//            internal fun provideCategoryRepository(
//                service: CategoryService,
//                categoryScheduler: CategoryScheduler
//            ): CategoryRepository {
//                return CategoryRepositoryImpl(service, categoryScheduler)
//            }

            @CategoryScope
            @Provides
            @JvmStatic
            internal fun router(
                component: Component,
                view: CategoryView,
                interactor: CategoryInteractor
            ): CategoryRouter {
                return CategoryRouter(view, interactor, component)
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @CategoryScope
    @dagger.Component(
        modules = arrayOf(Module::class),
        dependencies = arrayOf(ParentComponent::class)
    )
    interface Component : InteractorBaseComponent<CategoryInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: CategoryInteractor): Builder

            @BindsInstance
            fun view(view: CategoryView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun categoryRouter(): CategoryRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class CategoryScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class CategoryInternal
}
