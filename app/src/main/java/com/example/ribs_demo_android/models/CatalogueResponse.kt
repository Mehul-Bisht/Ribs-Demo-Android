package com.example.ribs_demo_android.models

import com.google.gson.annotations.SerializedName

data class CatalogueResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("brawlerCount") val brawlerCount: Int,
    @SerializedName("brawlers") val brawlers: List<Catalogue>
)
