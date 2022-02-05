package com.imix.assessmentapp.api


import com.imix.assessmentapp.model.get_all_photos.GetAllPhotosResponse
import com.imix.assessmentapp.model.get_single_photo_detail.GetSinglePhotoDetailsResponse
import com.imix.assessmentapp.model.get_suggested_photos.GetSuggestedPhotosResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {


    @GET("get_all_photos")
    suspend fun getAllPhotos(
    ): Response<GetAllPhotosResponse>


    @FormUrlEncoded
    @POST("get_single_photo_detail")
    suspend fun getSinglePhotoDetails(
        @Field("photo_id") photo_id: String?,
    ): Response<GetSinglePhotoDetailsResponse>


    @FormUrlEncoded
    @POST("get_suggested_photos")
    suspend fun getSuggestedPhotos(
        @Field("photo_id") photo_id: String?,
    ): Response<GetSuggestedPhotosResponse>


}