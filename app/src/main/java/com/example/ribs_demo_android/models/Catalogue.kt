package com.example.ribs_demo_android.models

import com.google.gson.annotations.SerializedName

data class Catalogue(
    @SerializedName("name") val name: String,
    @SerializedName("rarity") val rarity: String,
    @SerializedName("imageUrl") val imageUrl: String
)
