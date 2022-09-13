package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.CategoryService
import com.example.ribs_demo_android.ribs.root.category.CategoryScheduler
import com.example.ribs_demo_android.ribs.root.repository.CategoryRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class CategoryRepositoryImpl(
    private val categoryService: CategoryService,
    private val categoryScheduler: CategoryScheduler
) : CategoryRepository {

    override fun getByRarity(rarity: String): Observable<Resource<CatalogueResponse>> {
        return object : Observable<Resource<CatalogueResponse>>() {
            override fun subscribeActual(observer: Observer<in Resource<CatalogueResponse>>?) {
                observer?.onNext(Resource.Loading())
                categoryService.getByRarity(rarity)
                    .subscribeOn(categoryScheduler.io)
                    .observeOn(categoryScheduler.main)
                    .subscribe(
                        object : Consumer<CatalogueResponse> {
                            override fun accept(t: CatalogueResponse?) {
                                t?.let {
                                    observer?.onNext(Resource.Success(it))
                                }
                            }
                        }
                    ) {
                        observer?.onNext(Resource.Error(it.message.toString()))
                    }
            }
        }
    }
}