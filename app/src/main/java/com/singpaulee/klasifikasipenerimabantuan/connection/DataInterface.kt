package com.singpaulee.klasifikasipenerimabantuan.connection

import com.singpaulee.klasifikasipenerimabantuan.model.ResponseDataTraining
import com.singpaulee.klasifikasipenerimabantuan.model.ResponseQuestionnaire
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface DataInterface {

    @GET("exec")
    fun getQuestionnaire(
        @Query("action") action: String?
    ): Observable<ResponseQuestionnaire>

    @GET("exec")
    fun getListData(
        @Query("action") action: String?
    ): Observable<ResponseDataTraining>
}