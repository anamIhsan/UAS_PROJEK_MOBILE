package id.ihsan.uts_projek.api

import id.ihsan.uts_projek.model.ApiResponseKlasemen
import id.ihsan.uts_projek.model.ApiResponsePemain
import id.ihsan.uts_projek.model.ApiResponsePertandingan
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("pertandingan")
    fun getPertandingan(): Call<ApiResponsePertandingan>

    @GET("klasemen")
    fun getKlasemen(): Call<ApiResponseKlasemen>

    @GET("pemain")
    fun getPemain(): Call<ApiResponsePemain>
}