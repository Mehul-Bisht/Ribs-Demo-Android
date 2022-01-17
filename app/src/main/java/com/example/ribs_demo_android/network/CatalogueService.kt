package com.example.ribs_demo_android.network

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogueService {

    @GET("/allBrawlers")
    fun getAll(
        @Query("page") page: Int
    ): Observable<CatalogueResponse>

    @GET("/{name}")
    fun getDetail(
        @Path("name") name: String
    ): Observable<CatalogueDetail>
}