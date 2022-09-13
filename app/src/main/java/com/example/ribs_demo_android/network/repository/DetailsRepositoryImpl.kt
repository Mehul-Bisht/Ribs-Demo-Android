package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsScheduler
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer

class DetailsRepositoryImpl(
    private val detailsService: DetailsService,
    private val detailsScheduler: DetailsScheduler
) : DetailsRepository {

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return object : Observable<Resource<CatalogueDetail>>() {
            override fun subscribeActual(observer: Observer<in Resource<CatalogueDetail>>?) {
                observer?.onNext(Resource.Loading())
                detailsService.getDetail(name)
                    .subscribeOn(detailsScheduler.io)
                    .observeOn(detailsScheduler.main)
                    .subscribe(
                        object : Consumer<CatalogueDetail> {
                            override fun accept(t: CatalogueDetail?) {
                                t?.let {
                                    observer?.onNext(Resource.Success(it))
                                }
                            }
                        }
                    ) {
                        observer?.onNext(Resource.Error(it.message.toString()))
                    }
            }
        }
    }
}