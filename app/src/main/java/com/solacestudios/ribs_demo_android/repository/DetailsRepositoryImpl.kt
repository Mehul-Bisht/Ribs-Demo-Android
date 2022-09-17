package com.solacestudios.ribs_demo_android.repository

import com.solacestudios.ribs_demo_android.models.CatalogueDetail
import com.solacestudios.ribs_demo_android.network.DetailsService
import com.solacestudios.ribs_demo_android.network.createResult
import com.solacestudios.ribs_demo_android.ribs.details.DetailsScheduler
import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable

class DetailsRepositoryImpl(
    private val detailsService: DetailsService,
    private val detailsScheduler: DetailsScheduler
) : DetailsRepository {

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return createResult(detailsService.getDetail(name))
    }
}