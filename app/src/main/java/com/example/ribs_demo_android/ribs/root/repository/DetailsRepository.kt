package com.example.ribs_demo_android.ribs.root.repository

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable

interface DetailsRepository {

    fun getDetail(name: String): Observable<Resource<CatalogueDetail>>
}