package com.imix.assessmentapp.model.get_suggested_photos

data class GetSuggestedPhotosResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)