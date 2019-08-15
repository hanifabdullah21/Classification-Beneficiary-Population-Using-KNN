package com.singpaulee.klasifikasipenerimabantuan.model

import com.google.gson.annotations.SerializedName

data class ResponseQuestionnaire(

    @field:SerializedName("data")
    val data: QuessionnaireListModel? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)