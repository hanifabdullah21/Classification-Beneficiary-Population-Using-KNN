package com.singpaulee.klasifikasipenerimabantuan.connection

import com.singpaulee.klasifikasipenerimabantuan.model.ResponseDataTraining
import com.singpaulee.klasifikasipenerimabantuan.model.ResponseQuestionnaire
import io.reactivex.Observable
import retrofit2.http.*

interface DataInterface {

    @GET("exec")
    fun getQuestionnaire(
        @Query("action") action: String?
    ): Observable<ResponseQuestionnaire>

    @GET("exec")
    fun getListData(
        @Query("action") action: String?
    ): Observable<ResponseDataTraining>

    @FormUrlEncoded
    @POST("exec")
    fun postClassificationData(
        @Field("name") name: String,
        @Field("education") education: Int?,
        @Field("family") family: Int?,
        @Field("job") job: Int?,
        @Field("income") income: Int?,
        @Field("house") house: Int?,
        @Field("status") status: String?
    ): Observable<ResponseDataTraining>

}