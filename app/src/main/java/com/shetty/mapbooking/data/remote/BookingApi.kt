package com.shetty.mapbooking.data.remote

import com.shetty.mapbooking.data.model.BookDetails
import com.shetty.mapbooking.data.model.BookRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookingApi {

    @POST("books")
    suspend fun createBook(
        @Body request: BookRequest
    ): BookDetails

    @GET("books")
    suspend fun getBooks(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<BookDetails>
}