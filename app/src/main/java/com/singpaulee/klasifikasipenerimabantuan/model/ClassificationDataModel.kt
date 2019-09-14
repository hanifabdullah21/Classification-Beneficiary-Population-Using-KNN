package com.singpaulee.klasifikasipenerimabantuan.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassificationDataModel(

    @field:SerializedName("id")
    var id : Int? = null,

    @field:SerializedName("nama")
    var name: String? = null,

    @field:SerializedName("last_education")
    var education : QuessionnareModel? = null,

    @field:SerializedName("total_family")
    var family : QuessionnareModel? = null,

    @field:SerializedName("job")
    var job : QuessionnareModel? = null,

    @field:SerializedName("total_income")
    var income : QuessionnareModel? = null,

    @field:SerializedName("house_condition")
    var house : QuessionnareModel? = null,

    @field:SerializedName("status")
    var status: String? = null,

    var distance: Double? = null

) : Parcelable