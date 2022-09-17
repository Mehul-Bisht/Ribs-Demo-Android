package com.solacestudios.ribs_demo_android.ribs.catalogue

import android.util.Log
import com.solacestudios.ribs_demo_android.models.Catalogue
import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.network.CatalogueService
import com.solacestudios.ribs_demo_android.ribs.details.DetailsInteractor
import com.solacestudios.ribs_demo_android.repository.CatalogueRepository
import com.solacestudios.ribs_demo_android.util.Resource
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [CatalogueScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class CatalogueInteractor : Interactor<CatalogueInteractor.CataloguePresenter, CatalogueRouter>() {
    companion object {
        const val TAG = "CatalogueInteractor"
    }
    @Inject
    lateinit var presenter: CataloguePresenter

    @Inject
    lateinit var catalogueService: CatalogueService

    @Inject
    lateinit var catalogueListener: CatalogueListener

    @Inject
    lateinit var catalogueRepository: CatalogueRepository

    @Inject @CatalogueBuilder.CatalogueInternal
    lateinit var dataStream: MutableDataStream

    @Inject
    lateinit var catalogueScheduler: CatalogueScheduler

    private var disposables = CompositeDisposable()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).

        getInitialData(1)
            .observeOn(catalogueScheduler.main)
            .doOnSubscribe { disposables.add(it) }
            .subscribe({result -> handleResult(result)}) {
                Log.e(TAG, it.toString())
            }

        handleCategoryToggle()
        handleCategoryTaps()
    }

    private fun handleCategoryTaps() {
        presenter.categoryTaps()
            .doOnSubscribe { disposables.add(it) }
            .observeOn(catalogueScheduler.main)
            .subscribe({ category ->
                    Log.e(this.javaClass.name, "itemClickedObserved: $category")
                    router.attachDetails()
                    dataStream.setData(category)
                }) {Log.e(TAG, it.toString())}
    }

    private fun handleResult(state: Resource<CatalogueResponse>) {
        when (state) {
            is Resource.Loading -> {
                presenter.updateProgressbarState(true)
            }
            is Resource.Success -> {
                presenter.updateProgressbarState(false)
                presenter.setupUI(state.data!!.brawlers)
            }
            is Resource.Error -> {
                presenter.updateProgressbarState(false)
            }
        }
    }

    fun getInitialData(page: Int): Observable<Resource<CatalogueResponse>> {
        return catalogueRepository.getAll(1)
    }

    fun handleCategoryToggle() {
        presenter.onCategoryToggle()
            .subscribeOn(catalogueScheduler.io)
            .observeOn(catalogueScheduler.main)
            .doOnSubscribe { disposables.add(it) }
            .subscribe({catalogueListener.onClick()}) {
                Log.e(TAG, "handleCategoryToggle:: $it")
            }
    }

    override fun willResignActive() {
        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
        super.willResignActive()

        if (!disposables.isDisposed){
            disposables.dispose()
            disposables = CompositeDisposable()
        }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CataloguePresenter {
        fun setupUI(items: List<Catalogue>)
        fun updateProgressbarState(isVisible: Boolean)
        fun onCategoryToggle(): Observable<Boolean>
        fun categoryTaps(): Observable<String>
    }

    interface CatalogueListener {
        fun onClick()
    }

    inner class DetailsListener: DetailsInteractor.DetailsListener {
        override fun onBackPress() {
            router.detachDetails()
        }
    }
}
