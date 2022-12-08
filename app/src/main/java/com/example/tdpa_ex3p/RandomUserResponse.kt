package com.example.tdpa_ex3p

import com.google.gson.annotations.SerializedName

data class RandomUserResponse(
    @SerializedName("results") val datos: List<Results>
)

data class Results(
    @SerializedName("picture") val picture: Picture
)

data class Picture(
    @SerializedName("large") val imagenUrl: String
)