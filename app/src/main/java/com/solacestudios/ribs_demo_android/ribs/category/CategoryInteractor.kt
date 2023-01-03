package com.solacestudios.ribs_demo_android.ribs.category

import android.util.Log
import com.solacestudios.ribs_demo_android.models.Catalogue
import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.network.CategoryService
import com.solacestudios.ribs_demo_android.repository.CategoryRepositoryImpl
import com.solacestudios.ribs_demo_android.repository.CategoryRepository
import com.solacestudios.ribs_demo_android.util.Resource
import com.solacestudios.ribs_demo_android.util.RibsScheduler
import com.solacestudios.ribs_demo_android.util.RibsSchedulerImpl
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
class CategoryInteractor : Interactor<CategoryInteractor.Presenter, CategoryRouter>() {
    companion object {
        const val TAG = "CategoryInteractor"
        var categoryScheduler: RibsScheduler = RibsSchedulerImpl()
    }
    @Inject
    lateinit var buildPresenter: Presenter

    @Inject
    lateinit var categoryService: CategoryService

    @Inject
    lateinit var listener: Listener

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
        Log.e(TAG, "Entered init function")

        categoryRepository = CategoryRepositoryImpl(categoryService)
    }

    fun handleToggle() {
        buildPresenter.toggle()
            .doOnSubscribe { disposables.add(it)}
            .subscribe({
                listener.toggleCategory()
                Log.e(TAG, "handleToggle::$it ::Thread:: ${Thread.currentThread().name}")
            }) {
                Log.e(TAG, "handleToggleFailed::$it ::Thread:: ${Thread.currentThread().name}")
            }
    }

    fun getChip() {
        buildPresenter.getChip()
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
                buildPresenter.updateProgressbarState(true)
            }
            is Resource.Success -> {
                buildPresenter.setup(result.data?.brawlers!!)
                buildPresenter.updateProgressbarState(false)
            }
            is Resource.Error -> {
                buildPresenter.updateProgressbarState(false)
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
    interface Presenter {
        fun setup(items: List<Catalogue>)
        fun toggle(): Observable<Boolean>
        fun getChip(): Observable<String>
        fun updateProgressbarState(isVisible: Boolean)
    }

    interface Listener {
        fun toggleCategory()
    }
}
