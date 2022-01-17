package com.example.ribs_demo_android.network.repository

import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.network.FakeResponse
import com.example.ribs_demo_android.ribs.root.repository.CatalogueRepository
import com.example.ribs_demo_android.util.Resource
import io.reactivex.Observable

class FakeCatalogueRepository: CatalogueRepository {

    override fun getAll(page: Int): Observable<Resource<CatalogueResponse>> {
        return Observable.just(FakeResponse.FAKE_RESOURCE_CATALOGUE)
    }

    override fun getDetail(name: String): Observable<Resource<CatalogueDetail>> {
        return Observable.just(
            Resource.Success(
                CatalogueDetail(
                    name = "Amber",
                    rarity = "legendary",
                    imageUrl = "",
                    type = "fire",
                    hp = "4700"
                )
            )
        )
    }
}