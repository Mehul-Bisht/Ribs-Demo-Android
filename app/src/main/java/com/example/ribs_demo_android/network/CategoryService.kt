package com.example.ribs_demo_android.network

import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Mehul Bisht on 04-01-2022
 */
interface CategoryService {

    @GET("/{rarity}")
    fun getByRarity(
        @Path("rarity") rarity: String
    ): Observable<CatalogueResponse>

    @GET("/{name}")
    fun getDetail(
        @Path("name") page: Int
    ): Observable<CatalogueDetail>
}