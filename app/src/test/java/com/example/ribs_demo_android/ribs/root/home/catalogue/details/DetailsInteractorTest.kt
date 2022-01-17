package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.network.FakeResponse
import com.example.ribs_demo_android.network.repository.DetailsRepositoryImpl
import com.example.ribs_demo_android.ribs.root.home.catalogue.DataStream
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.example.ribs_demo_android.util.Resource
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import java.io.IOException

class DetailsInteractorTest {

    @Mock lateinit var service: DetailsService
    @Mock lateinit var repository: DetailsRepository
    @Mock lateinit var presenter: DetailsInteractor.DetailsPresenter
    @Mock lateinit var datastream: DataStream
    @Mock lateinit var listener: DetailsInteractor.DetailsListener

    lateinit var interactor: DetailsInteractor
    private val scheduler = FakeDetailsScheduler()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        interactor = DetailsInteractor()

        repository = DetailsRepositoryImpl(service, scheduler)

        interactor.detailsScheduler = scheduler
        interactor.detailsRepository = repository
        interactor.dataStream = datastream
        interactor.service = service
        interactor.presenter = presenter
        interactor.detailsListener = listener
    }

    @Test
    fun `test success state from repository`() {
        `when`(datastream.data()).thenReturn(Observable.just("Amber"))
        `when`(service.getDetail(anyString())).thenReturn(Observable.just(FakeResponse.FAKE_DETAILS_RESPONSE))
        interactor.setupUI()

        val testObserver = repository.getDetail("Amber").test()
        testObserver.assertValueCount(2)
        val last = testObserver.values().last()

        when(last) {
            is Resource.Success -> {
                assert(last.data == FakeResponse.FAKE_DETAILS_RESPONSE)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test loading state from repository`() {
        `when`(datastream.data()).thenReturn(Observable.just("Amber"))
        `when`(service.getDetail(anyString())).thenReturn(Observable.just(FakeResponse.FAKE_DETAILS_RESPONSE))
        interactor.setupUI()

        val testObserver = repository.getDetail("Amber").test()
        testObserver.assertValueCount(2)
        val first = testObserver.values().first()

        when(first) {
            is Resource.Loading -> {
                assert(true)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test error state from repository`() {
        `when`(datastream.data()).thenReturn(Observable.just("Amber"))
        `when`(service.getDetail(anyString())).thenReturn(Observable.error(IOException()))
        interactor.setupUI()

        val testObserver = repository.getDetail("Amber").test()
        testObserver.assertValueCount(2)
        val last = testObserver.values().last()

        when(last) {
            is Resource.Error -> {
                assert(true)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `on back press should call listener`() {
        `when`(presenter.onBack()).thenReturn(Observable.just(true))
        interactor.onBack()

        verify(listener).onBackPress()
    }
}