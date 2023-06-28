package com.example.characterlistexercise.network

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("RelatedTopics")
    val relatedTopics: List<CharacterInfo>
)

data class CharacterInfo(
    @SerializedName("FirstURL")
    val firstUrl: String,
    @SerializedName("Icon")
    val icon: Icon,
    @SerializedName("Result")
    val result: String,
    @SerializedName("Text")
    val text: String,
)

data class Icon(
    @SerializedName("Height")
    val height: String,
    @SerializedName("URL")
    val url: String,
    @SerializedName("Width")
    val width: String,
)