package edu.du.week6networking

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface BikeService {
    @POST("bikes")
    suspend fun createBike(@Body requestBody: RequestBody): Response<ResponseBody>

    @GET("bikes")
    suspend fun getBikes(): Response<ResponseBody>

    @GET("bikes/{id}")
    suspend fun getBike(@Path("id") id: String): Response<ResponseBody>

    @PUT("bikes/{id}")
    suspend fun updateBike(@Path("id") id: String, @Body requestBody: RequestBody): Response<ResponseBody>

    @DELETE("bikes/{id}")
    suspend fun deleteBike(@Path("id") id: String): Response<ResponseBody>

}