package com.solacestudios.ribs_demo_android.network.repository

import com.solacestudios.ribs_demo_android.models.CatalogueDetail
import com.solacestudios.ribs_demo_android.models.CatalogueResponse
import com.solacestudios.ribs_demo_android.network.FakeResponse
import com.solacestudios.ribs_demo_android.repository.CatalogueRepository
import com.solacestudios.ribs_demo_android.util.Resource
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