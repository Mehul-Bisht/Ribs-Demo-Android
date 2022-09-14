package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.network.DetailsService
import com.example.ribs_demo_android.network.createResult
import com.example.ribs_demo_android.ribs.root.home.catalogue.details.DetailsScheduler
import com.example.ribs_demo_android.ribs.root.repository.DetailsRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable

class DetailsRepositoryImpl(
    private val detailsService: DetailsService,
    private val detailsScheduler: DetailsScheduler
) : DetailsRepository {

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return createResult(detailsService.getDetail(name))
    }
}