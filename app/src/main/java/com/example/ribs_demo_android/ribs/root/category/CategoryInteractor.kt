package com.example.ribs_demo_android.ribs.root.category

import androidx.annotation.VisibleForTesting
import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CategoryService
import com.example.ribs_demo_android.network.repository.CategoryRepositoryImpl
import com.example.ribs_demo_android.ribs.root.repository.CategoryRepository
import com.example.ribs_demo_android.util.Resource
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [CategoryScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class CategoryInteractor : Interactor<CategoryInteractor.CategoryPresenter, CategoryRouter>() {

    @Inject
    lateinit var presenter: CategoryPresenter

    @Inject
    lateinit var categoryService: CategoryService

    @Inject
    lateinit var categoryToggleListener: CategoryToggleListener

    @Inject
    @Named("category")
    lateinit var categoryScheduler: CategoryScheduler

//    @Inject
//    @Named("categoryRepo")
//    lateinit var categoryRepository: CategoryRepository

    lateinit var categoryRepository: CategoryRepository

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
        init()

        getByRarity("chromatic")

        handleToggle()

        getChip()
    }

    fun init() {
        categoryRepository = CategoryRepositoryImpl(categoryService, categoryScheduler)
    }

    fun handleToggle() {
        presenter.toggle()
            .subscribeOn(categoryScheduler.io)
            .observeOn(categoryScheduler.main)
            .subscribe(object : Consumer<Boolean> {
                override fun accept(t: Boolean?) {
                    t?.let {
                        categoryToggleListener.toggleCategory()
                    }
                }
            })
    }

    fun getChip() {
        presenter.getChip()
            .subscribeOn(categoryScheduler.io)
            .observeOn(categoryScheduler.main)
            .subscribe(object : Consumer<String> {
                override fun accept(t: String?) {
                    t?.let {
                        getByRarity(it)
                    }
                }
            }
            ) {
                it.printStackTrace()
            }
    }

    fun getByRarity(rarity: String) {
        categoryRepository.getByRarity(rarity)
            .subscribeOn(categoryScheduler.io)
            .observeOn(categoryScheduler.main)
            .subscribe(
                object : Consumer<Resource<CatalogueResponse>> {
                    override fun accept(t: Resource<CatalogueResponse>?) {
                        t?.let {
                            when (it) {
                                is Resource.Loading -> {
                                    presenter.updateProgressbarState(true)
                                }
                                is Resource.Success -> {
                                    presenter.setup(it.data?.brawlers!!)
                                    presenter.updateProgressbarState(false)
                                }
                                is Resource.Error -> {
                                    presenter.updateProgressbarState(false)
                                }
                            }
                        }
                    }
                }
            ) {
                it.printStackTrace()
            }
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CategoryPresenter {
        fun setup(items: List<Catalogue>)
        fun toggle(): Observable<Boolean>
        fun getChip(): Observable<String>
        fun updateProgressbarState(isVisible: Boolean)
    }

    interface CategoryToggleListener {
        fun toggleCategory()
    }
}
