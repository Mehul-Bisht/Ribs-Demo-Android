package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CategoryService
import com.example.ribs_demo_android.network.createResult
import com.example.ribs_demo_android.ribs.root.category.CategoryScheduler
import com.example.ribs_demo_android.ribs.root.repository.CategoryRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable

class CategoryRepositoryImpl(
    private val categoryService: CategoryService,
    private val categoryScheduler: CategoryScheduler
) : CategoryRepository {

    override fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>> {
        return createResult(categoryService.getByRarity(rarity))
    }
}