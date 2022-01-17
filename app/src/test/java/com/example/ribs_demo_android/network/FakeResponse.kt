package com.example.ribs_demo_android.network

import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.models.CatalogueDetail
import com.example.ribs_demo_android.models.CatalogueResponse
import com.example.ribs_demo_android.util.Resource

object FakeResponse {

    val FAKE_CATALOGUE_RESPONSE = CatalogueResponse(
        title = "Dummy title",
        description = "Dummy description",
        brawlerCount = 1,
        brawlers = listOf(
            Catalogue(
                name = "Amber",
                rarity = "legendary",
                imageUrl = ""
            )
        )
    )

    val FAKE_CATALOGUE_ERROR = CatalogueResponse(
        title = "Dummy title",
        description = "Dummy description",
        brawlerCount = 1,
        brawlers = listOf(
            Catalogue(
                name = "Amber",
                rarity = "legendary",
                imageUrl = ""
            )
        )
    )

    val FAKE_DETAILS_RESPONSE = CatalogueDetail(
        name = "Amber",
        rarity = "legendary",
        imageUrl = "",
        type = "fire",
        hp = "4700"
    )

    val FAKE_RESOURCE_CATALOGUE = Resource.Success(FAKE_CATALOGUE_RESPONSE)

    val FAKE_CATALOGUE_LIST = listOf(
        Catalogue(
            name = "Amber",
            rarity = "legendary",
            imageUrl = ""
        )
    )
}