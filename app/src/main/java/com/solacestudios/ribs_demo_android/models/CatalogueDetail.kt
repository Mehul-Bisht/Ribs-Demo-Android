package com.solacestudios.ribs_demo_android.models

import com.google.gson.annotations.SerializedName

data class CatalogueDetail(
    @SerializedName("name") val name: String,
    @SerializedName("rarity") val rarity: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("type") val type: String,
    @SerializedName("hp") val hp: String
)
