package com.imix.assessmentapp.model.get_all_photos

data class GetAllPhotosResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)