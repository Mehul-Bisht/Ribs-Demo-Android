package com.solacestudios.ribs_demo_android.repository

import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable


interface CategoryRepository {

    fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>>
}