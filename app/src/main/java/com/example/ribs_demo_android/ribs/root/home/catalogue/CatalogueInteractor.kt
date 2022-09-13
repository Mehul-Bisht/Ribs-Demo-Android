package com.example.ribs_demo_android.ribs.root.home.catalogue

import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsInteractor
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.util.Resource
import com.uber.autodispose.AutoDispose
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * Coordinates Business Logic for [CatalogueScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class CatalogueInteractor : Interactor<CatalogueInteractor.CataloguePresenter, CatalogueRouter>() {

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

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).

        getInitialData(1)
            .subscribeOn(catalogueScheduler.io)
            .observeOn(catalogueScheduler.main)
            .`as`(AutoDispose.autoDisposable(this))
            .subscribe(
                object : Consumer<Resource<CatalogueResponse>> {
                    override fun accept(t: Resource<CatalogueResponse>?) {
                        t?.let { state ->
                            when(state) {
                                is Resource.Loading -> {
                                    presenter.updateProgressbarState(true)
                                }
                                is Resource.Success -> {
                                    presenter.updateProgressbarState(false)
                                    presenter.setupUI(state.data!!.brawlers)
                                        .subscribeOn(catalogueScheduler.io)
                                        .observeOn(catalogueScheduler.main)
                                        .subscribe(
                                            object : Consumer<String> {
                                                override fun accept(t: String?) {
                                                    t?.let {
                                                        router.attachDetails()
                                                        dataStream.setData(it)
                                                    }
                                                }
                                            }
                                        ) {
                                            it.printStackTrace()
                                        }
                                }
                                is Resource.Error -> {
                                    presenter.updateProgressbarState(false)
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            ) {
                it.printStackTrace()
            }

        handleCategoryToggle()
    }

    fun getInitialData(page: Int): Observable<Resource<CatalogueResponse>> {
        return catalogueRepository.getAll(1)
    }

    fun handleCategoryToggle() {
        presenter.onCategoryToggle()
            .subscribeOn(catalogueScheduler.io)
            .observeOn(catalogueScheduler.main)
            .`as`(AutoDispose.autoDisposable(this))
            .subscribe(object: Consumer<Boolean> {
                override fun accept(t: Boolean?) {
                    t?.let {
                        catalogueListener.onClick()
                    }
                }
            })
    }

    override fun willResignActive() {
        super.willResignActive()

        // TODO: Perform any required clean up here, or delete this method entirely if not needed.
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CataloguePresenter {
        fun setupUI(items: List<Catalogue>): Observable<String>
        fun updateProgressbarState(isVisible: Boolean)
        fun onCategoryToggle(): Observable<Boolean>
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
