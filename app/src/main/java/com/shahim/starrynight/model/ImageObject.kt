package com.shahim.starrynight.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

data class ImageObject (
    val copyright: String?,
    @SerializedName("date") private val _date: String?,
    val explanation: String?,
    @SerializedName("hdurl") val hdUrl: String?,
    @SerializedName("media_type") val mediaType: String?,
    val title: String,
    val url: String) {

    val date : LocalDate
    get() {
        return LocalDate.parse(_date)
    }
}
