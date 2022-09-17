package com.solacestudios.ribs_demo_android.ribs.root.category

import com.solacestudios.ribs_demo_android.network.CategoryService
import com.solacestudios.ribs_demo_android.network.FakeResponse
import com.solacestudios.ribs_demo_android.repository.CategoryRepositoryImpl
import com.solacestudios.ribs_demo_android.ribs.category.CategoryInteractor
import com.solacestudios.ribs_demo_android.repository.CategoryRepository
import com.solacestudios.ribs_demo_android.util.Resource
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.MockitoAnnotations
import java.io.IOException

class CategoryInteractorTest {

    @Mock lateinit var service: CategoryService
    @Mock lateinit var repository: CategoryRepository
    @Mock lateinit var presenter: CategoryInteractor.CategoryPresenter
    @Mock lateinit var listener: CategoryInteractor.CategoryToggleListener

    lateinit var interactor: CategoryInteractor
    private val scheduler = FakeCategoryScheduler()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        repository = CategoryRepositoryImpl(service, scheduler)

        interactor = CategoryInteractor()
        interactor.categoryService = service
        interactor.categoryRepository = repository
        interactor.presenter = presenter
        interactor.categoryScheduler = scheduler
        interactor.categoryToggleListener = listener
    }

    @Test
    fun `test success state from repository`() {
        `when`(presenter.getChip()).thenReturn(Observable.just("Amber"))
        `when`(service.getByRarity(anyString())).thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))
        interactor.getChip()

        val testObserver = repository.getByRarity("Amber").test()
        val last = testObserver.values().last()

        when(last) {
            is Resource.Success -> {
                assert(last.data == FakeResponse.FAKE_CATALOGUE_RESPONSE)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test loading state from repository`() {
        `when`(presenter.getChip()).thenReturn(Observable.just("Amber"))
        `when`(service.getByRarity(anyString())).thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))
        interactor.getChip()

        val testObserver = repository.getByRarity("Amber").test()
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
        `when`(presenter.getChip()).thenReturn(Observable.just("Amber"))
        `when`(service.getByRarity(anyString())).thenReturn(Observable.error(IOException()))
        interactor.getChip()

        val testObserver = repository.getByRarity("Amber").test()
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
    fun `test category toggle, should call listener`() {
        `when`(presenter.toggle()).thenReturn(Observable.just(true))
        interactor.handleToggle()

        verify(listener).toggleCategory()
    }
}