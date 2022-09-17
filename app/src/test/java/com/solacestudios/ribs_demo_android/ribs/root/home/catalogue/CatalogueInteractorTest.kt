package com.solacestudios.ribs_demo_android

import com.solacestudios.ribs_demo_android.network.CatalogueService
import com.solacestudios.ribs_demo_android.network.FakeResponse
import com.solacestudios.ribs_demo_android.repository.CatalogueRepositoryImpl
import com.solacestudios.ribs_demo_android.ribs.catalogue.CatalogueInteractor
import com.solacestudios.ribs_demo_android.ribs.catalogue.CatalogueRouter
import com.solacestudios.ribs_demo_android.ribs.catalogue.MutableDataStream
import com.solacestudios.ribs_demo_android.ribs.root.home.catalogue.*
import com.solacestudios.ribs_demo_android.repository.CatalogueRepository
import com.solacestudios.ribs_demo_android.util.Resource
import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException

class CatalogueInteractorTest {

    @Mock
    lateinit var presenter: CatalogueInteractor.CataloguePresenter
    @Mock
    lateinit var repository: CatalogueRepository
    @Mock
    lateinit var service: CatalogueService
    @Mock
    lateinit var listener: CatalogueInteractor.CatalogueListener
    @Mock
    lateinit var dataStream: MutableDataStream
    @Mock
    lateinit var router: CatalogueRouter

    private val fakeScheduler = FakeCatalogueScheduler()

    lateinit var interactor: CatalogueInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = CatalogueRepositoryImpl(service, fakeScheduler)

        interactor = CatalogueInteractor()
        interactor.catalogueRepository = repository
        interactor.presenter = presenter
        interactor.catalogueScheduler = fakeScheduler
        interactor.catalogueListener = listener
        interactor.dataStream = dataStream
    }

    @Test
    fun `test success state from repository`() {
        `when`(presenter.setupUI(anyList())).thenReturn(Observable.just("Amber"))
        `when`(service.getAll(anyInt())).thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))

        val testObserver = interactor.getInitialData(1).test()
        testObserver.assertValueCount(2)
        val last = testObserver.values().last()

        when(last) {
            is Resource.Success -> {
                assertThat(last.data).isEqualTo(FakeResponse.FAKE_CATALOGUE_RESPONSE)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test loading state from repository`() {
        `when`(presenter.setupUI(anyList())).thenReturn(Observable.just("Amber"))
        `when`(service.getAll(anyInt())).thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))

        val testObserver = interactor.getInitialData(1).test()
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
        `when`(presenter.setupUI(anyList())).thenReturn(Observable.just(""))
        `when`(service.getAll(anyInt())).thenReturn(Observable.error(IOException()))

        val testObserver = interactor.getInitialData(1).test()
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
    fun `test presenter returns correct key`() {
        `when`(presenter.setupUI(anyList())).thenReturn(Observable.just("Amber"))
        //`when`(service.getAll(anyInt())).thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))

        //interactor.getInitialData()

        val testObserver = presenter.setupUI(FakeResponse.FAKE_CATALOGUE_LIST).test()
        testObserver
            .assertOf {
                assertThat(it.values().size).isEqualTo(1)
                assertThat(it.values().first()).isNotEmpty()
                assertThat(it.values().first()).isEqualTo("Amber")
                //verify(dataStream).setData("Amber")
            }
    }

    @Test
    fun `test category toggle`() {
        `when`(presenter.onCategoryToggle()).thenReturn(Observable.just(true))

        interactor.handleCategoryToggle()

        verify(listener).onClick()
    }
}