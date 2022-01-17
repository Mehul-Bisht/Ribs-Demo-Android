package com.example.ribs_demo_android.network

import com.example.ribs_demo_android.models.CatalogueDetail
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsService {

    @GET("/{name}")
    fun getDetail(
        @Path("name") name: String
    ): Observable<CatalogueDetail>
}