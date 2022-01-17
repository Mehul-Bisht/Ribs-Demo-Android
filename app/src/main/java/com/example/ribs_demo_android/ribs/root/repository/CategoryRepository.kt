package com.example.ribs_demo_android.ribs.root.repository

import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable

interface CategoryRepository {

    fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>>
}