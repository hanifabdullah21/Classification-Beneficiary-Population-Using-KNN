package com.singpaulee.klasifikasipenerimabantuan.model

import com.google.gson.annotations.SerializedName

data class ResponseDataTraining(
    @field:SerializedName("data")
    val data: ArrayList<ClassificationDataModel>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)