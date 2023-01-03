package com.solacestudios.ribs_demo_android.repository

import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.network.CategoryService
import com.solacestudios.ribs_demo_android.network.createResult
import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable


interface CategoryRepository {

    fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>>
}

class CategoryRepositoryImpl(
    private val categoryService: CategoryService,
) : CategoryRepository {

    override fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>> {
        return createResult(categoryService.getByRarity(rarity))
    }
}