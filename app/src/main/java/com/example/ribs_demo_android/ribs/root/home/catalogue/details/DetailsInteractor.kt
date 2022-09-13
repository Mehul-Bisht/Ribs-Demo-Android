package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import android.util.Log
import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueScheduler
import com.example.ribs_demo_android.ribs.root.home.catalogue.DataStream
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.example.ribs_demo_android.util.Resource
import com.uber.autodispose.AutoDispose
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
 * Coordinates Business Logic for [DetailsScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class DetailsInteractor : Interactor<DetailsInteractor.DetailsPresenter, DetailsRouter>() {

    @Inject
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var service: DetailsService

    @Inject
    lateinit var detailsListener: DetailsListener

    @Inject
    lateinit var dataStream: DataStream

    @Inject
    lateinit var detailsScheduler: DetailsScheduler

    @Inject
    lateinit var detailsRepository: DetailsRepository

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
        setupUI()

        onBack()
    }

    fun setupUI() {
        dataStream.data()
            .`as`(AutoDispose.autoDisposable(this))
            .subscribe(
                object : Consumer<String> {
                    override fun accept(t: String?) {
                        t?.let {
                            detailsRepository.getDetail(it)
                                .subscribeOn(detailsScheduler.io)
                                .observeOn(detailsScheduler.main)
                                .subscribe(
                                    object : Consumer<Resource<CatalogueDetail>> {
                                        override fun accept(t: Resource<CatalogueDetail>?) {
                                            t?.let {
                                                when (it) {
                                                    is Resource.Loading -> {
                                                        presenter.updateProgressbarState(true)
                                                    }
                                                    is Resource.Success -> {
                                                        presenter.setData(it.data!!)
                                                        presenter.updateProgressbarState(false)
                                                    }
                                                    is Resource.Error -> {
                                                        presenter.updateProgressbarState(false)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                ) { e ->
                                    e.printStackTrace()
                                }
                        }
                    }
                }
            ) {
                it.printStackTrace()
            }
    }

    fun onBack() {
        presenter.onBack()
            .subscribeOn(detailsScheduler.io)
            .observeOn(detailsScheduler.main)
            .`as`(AutoDispose.autoDisposable(this))
            .subscribe(
                object : Consumer<Boolean> {
                    override fun accept(t: Boolean?) {
                        t?.let {
                            detailsListener.onBackPress()
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
    interface DetailsPresenter {
        fun onBack(): Observable<Boolean>
        fun setData(data: CatalogueDetail)
        fun updateProgressbarState(isVisible: Boolean)
    }

    interface DetailsListener {
        fun onBackPress()
    }
}
