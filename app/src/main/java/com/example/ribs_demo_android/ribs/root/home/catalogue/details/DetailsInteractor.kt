package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import android.util.Log
import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.ribs.root.home.catalogue.DataStream
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.example.ribs_demo_android.util.Resource
import com.uber.autodispose.AutoDispose
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DetailsScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class DetailsInteractor : Interactor<DetailsInteractor.DetailsPresenter, DetailsRouter>() {
    companion object {
        const val TAG = "DetailsInteractor"
    }

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
            .subscribe({fetchDetails(it)}) {
                Log.e(TAG, it.toString())
            }
    }

    private fun fetchDetails(detail: String) {
        detailsRepository.getDetail(detail)
            .subscribeOn(detailsScheduler.io)
            .observeOn(detailsScheduler.main)
            .subscribe({ res -> handleResult(res) }) {
                Log.e(TAG, it.toString())
            }
    }

    private fun handleResult(result: Resource<CatalogueDetail>) {
        when (result) {
            is Resource.Loading -> {
                presenter.updateProgressbarState(true)
            }
            is Resource.Success -> {
                presenter.setData(result.data!!)
                presenter.updateProgressbarState(false)
            }
            is Resource.Error -> {
                presenter.updateProgressbarState(false)
            }
        }
    }

    fun onBack() {
        presenter.onBack()
            .subscribeOn(detailsScheduler.main)
            .observeOn(detailsScheduler.main)
            .subscribe({detailsListener.onBackPress()}) {
                Log.e(TAG, it.toString())
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
