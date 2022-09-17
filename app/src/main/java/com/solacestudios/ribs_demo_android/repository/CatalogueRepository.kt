package com.solacestudios.ribs_demo_android.repository

import com.solacestudios.ribs_demo_android.models.CatalogueDetail
import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable

interface CatalogueRepository {

    fun getAll(page: Int): Observable<Resource<CatalogueResponse>>

    fun getDetail(name: String): Observable<Resource<CatalogueDetail>>
}