package com.example.ribs_demo_android.ribs.root.category

import android.util.Log
import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CategoryService
import com.example.ribs_demo_android.network.repository.CategoryRepositoryImpl
import com.example.ribs_demo_android.ribs.root.repository.CategoryRepository
import com.example.ribs_demo_android.util.Resource
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [CategoryScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class CategoryInteractor : Interactor<CategoryInteractor.CategoryPresenter, CategoryRouter>() {
    companion object {
        const val TAG = "CategoryInteractor"
    }
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
    private var disposables = CompositeDisposable()

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
            .doOnSubscribe { disposables.add(it)}
            .subscribe({
                categoryToggleListener.toggleCategory()
            }) {
                Log.e(TAG, "handleToggleFailed::$it ::Thread:: ${Thread.currentThread().name}")
            }
    }

    fun getChip() {
        presenter.getChip()
            .doOnSubscribe { disposables.add(it)}
            .subscribeOn(categoryScheduler.io)
            .observeOn(categoryScheduler.main)
            .subscribe({getByRarity(it)}){
                Log.e(TAG, "getChip$it")
            }
    }

    fun getByRarity(rarity: String) {
        categoryRepository.getByRarity(rarity)
            .subscribeOn(categoryScheduler.io)
            .doOnSubscribe { disposables.add(it)}
            .observeOn(categoryScheduler.main)
            .subscribe({handleResult(it)}) {
                Log.e(TAG, "getByRarity::$it")
            }
    }

    private fun handleResult(result: Resource<CatalogueResponse>) {
        when (result) {
            is Resource.Loading -> {
                presenter.updateProgressbarState(true)
            }
            is Resource.Success -> {
                presenter.setup(result.data?.brawlers!!)
                presenter.updateProgressbarState(false)
            }
            is Resource.Error -> {
                presenter.updateProgressbarState(false)
            }
        }
    }

    override fun willResignActive() {
        super.willResignActive()
        if (!disposables.isDisposed) {
            disposables.dispose()
            disposables = CompositeDisposable()
        }
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
