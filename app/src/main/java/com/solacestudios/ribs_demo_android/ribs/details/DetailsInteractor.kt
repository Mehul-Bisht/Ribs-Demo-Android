package com.solacestudios.ribs_demo_android.ribs.details

import android.util.Log
import com.solacestudios.ribs_demo_android.models.CatalogueDetail
import com.solacestudios.ribs_demo_android.network.DetailsService
import com.solacestudios.ribs_demo_android.util.DataStream
import com.solacestudios.ribs_demo_android.repository.DetailsRepository
import com.solacestudios.ribs_demo_android.util.Resource
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DetailsScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class DetailsInteractor : Interactor<DetailsInteractor.Presenter, DetailsRouter>() {
    companion object {
        const val TAG = "DetailsInteractor"
    }

    @Inject
    lateinit var buildPresenter: Presenter

    @Inject
    lateinit var service: DetailsService

    @Inject
    lateinit var detailsListener: Listener

    @Inject
    lateinit var dataStream: DataStream

    @Inject
    lateinit var detailsScheduler: DetailsScheduler

    @Inject
    lateinit var detailsRepository: DetailsRepository

    private var disposables = CompositeDisposable()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
        setupUI()

        onBack()
    }

    fun setupUI() {
        dataStream.data()
            .doOnSubscribe { disposables.add(it) }
            .subscribe({fetchDetails(it)}) {
                Log.e(TAG, it.toString())
            }
    }

    private fun fetchDetails(detail: String) {
        Log.e(this.javaClass.name, "fetchDetails::$detail")
        detailsRepository.getDetail(detail)
            .doOnSubscribe { disposables.add(it) }
            .subscribeOn(detailsScheduler.io)
            .observeOn(detailsScheduler.main)
            .subscribe({ res -> handleResult(res) }) {
                Log.e(TAG, it.toString())
            }
    }

    private fun handleResult(result: Resource<CatalogueDetail>) {
        when (result) {
            is Resource.Loading -> {
                buildPresenter.updateProgressbarState(true)
            }
            is Resource.Success -> {
                buildPresenter.setData(result.data!!)
                buildPresenter.updateProgressbarState(false)
            }
            is Resource.Error -> {
                buildPresenter.updateProgressbarState(false)
            }
        }
    }

    fun onBack() {
        buildPresenter.onBack()
            .subscribeOn(detailsScheduler.main)
            .observeOn(detailsScheduler.main)
            .doOnSubscribe { disposables.add(it) }
            .subscribe({detailsListener.onBackPress()}) {
                Log.e(TAG, it.toString())
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
        fun onBack(): Observable<Boolean>
        fun setData(data: CatalogueDetail)
        fun updateProgressbarState(isVisible: Boolean)
    }

    interface Listener {
        fun onBackPress()
    }
}
