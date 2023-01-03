package com.solacestudios.ribs_demo_android.network.repository

import com.solacestudios.ribs_demo_android.network.CatalogueService
import com.solacestudios.ribs_demo_android.network.FakeResponse
import com.solacestudios.ribs_demo_android.repository.CatalogueRepositoryImpl
import com.solacestudios.ribs_demo_android.ribs.root.home.catalogue.FakeCatalogueScheduler
import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

/**
 * Created by Mehul Bisht on 08-01-2022
 */

@RunWith(MockitoJUnitRunner::class)
class CatalogueRepositoryTest {

    @Mock
    lateinit var repository: CatalogueRepositoryImpl

    @Mock
    lateinit var service: CatalogueService

    private val scheduler = FakeCatalogueScheduler()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = CatalogueRepositoryImpl(service, scheduler)
    }

    @Test
    fun `test response is correct`() {
        `when`(service.getAll(anyInt()))
            .thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))

        val testObserver = repository
            .getAll(1)
            .test()

        testObserver.assertValueCount(2)

        val response = testObserver
            .values()
            .last()

        when (response) {
            is Resource.Success -> {
                assert(response.data == FakeResponse.FAKE_CATALOGUE_RESPONSE)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test exception state`() {
        `when`(service.getAll(anyInt()))
            .thenReturn(Observable.error(IOException()))

        val testObserver = repository
            .getAll(1)
            .test()

        testObserver.assertValueCount(2)

        val response = testObserver
            .values()
            .last()

        when (response) {
            is Resource.Error -> {
                assert(true)
            }
            else -> {
                assert(false)
            }
        }
    }

    @Test
    fun `test loading state`() {
        `when`(service.getAll(anyInt()))
            .thenReturn(Observable.just(FakeResponse.FAKE_CATALOGUE_RESPONSE))

        val testObserver = repository
            .getAll(1)
            .test()

        testObserver.assertValueCount(2)

        val response = testObserver
            .values()
            .first()

        when (response) {
            is Resource.Loading -> {
                assert(true)
            }
            else -> {
                assert(false)
            }
        }
    }
}