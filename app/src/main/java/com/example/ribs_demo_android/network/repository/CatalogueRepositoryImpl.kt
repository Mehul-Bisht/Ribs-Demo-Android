package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueScheduler
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class CatalogueRepositoryImpl(
    private val catalogueService: CatalogueService,
    private val scheduler: CatalogueScheduler
) : CatalogueRepository {

    override fun getAll(page: Int): Observable<Resource<CatalogueResponse>> {
        return object : Observable<Resource<CatalogueResponse>>() {
            override fun subscribeActual(observer: Observer<in Resource<CatalogueResponse>>?) {
                observer?.onNext(Resource.Loading())
                catalogueService.getAll(page)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.main)
                    .subscribe(
                        object : Consumer<CatalogueResponse> {
                            override fun accept(t: CatalogueResponse?) {
                                t?.let {
                                    observer?.onNext(Resource.Success(it))
                                }
                            }
                        },
                        {
                            observer?.onNext(Resource.Error(it.message.toString()))
                        }
                    )
            }
        }
    }

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return object : Observable<Resource<CatalogueDetail>>() {
            override fun subscribeActual(observer: Observer<in Resource<CatalogueDetail>>?) {
                observer?.onNext(Resource.Loading())
                catalogueService.getDetail(name)
                    .subscribeOn(scheduler.io)
                    .observeOn(scheduler.main)
                    .subscribe(
                        object : Consumer<CatalogueDetail> {
                            override fun accept(t: CatalogueDetail?) {
                                t?.let {
                                    observer?.onNext(Resource.Success(it))
                                }
                            }
                        },
                        {
                            observer?.onNext(Resource.Error(it.message.toString()))
                        }
                    )
            }
        }
    }
}