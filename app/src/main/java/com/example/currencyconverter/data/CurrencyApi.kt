package com.example.currencyconverter.data

import com.example.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("/latest/{base}")
    suspend fun getRates(
        @Path("base") base: String
    ): Response<CurrencyResponse>
}