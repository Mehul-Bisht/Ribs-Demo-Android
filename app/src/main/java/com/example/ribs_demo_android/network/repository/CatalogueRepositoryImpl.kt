package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CatalogueService
import com.example.ribs_demo_android.network.createResult
import com.example.ribs_demo_android.ribs.root.home.catalogue.CatalogueScheduler
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class CatalogueRepositoryImpl(
    private val catalogueService: CatalogueService,
    private val scheduler: CatalogueScheduler
) : CatalogueRepository {

    override fun getAll(page: Int): Observable<Resource<CatalogueResponse>> {
        return createResult(catalogueService.getAll(page))
    }

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return createResult(catalogueService.getDetail(name))
    }
}

