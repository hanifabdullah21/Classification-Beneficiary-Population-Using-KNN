package com.singpaulee.klasifikasipenerimabantuan.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuessionnareModel(
	@field:SerializedName("variabel")
	val variable: String? = null,

	@field:SerializedName("bobot")
	val weight: Int? = null
) : Parcelable

data class QuessionnaireListModel(
	@field:SerializedName("education")
	var education : ArrayList<QuessionnareModel>? = null,

	@field:SerializedName("family")
	var family : ArrayList<QuessionnareModel>? = null,

	@field:SerializedName("job")
	var job : ArrayList<QuessionnareModel>? = null,

	@field:SerializedName("income")
	var income : ArrayList<QuessionnareModel>? = null,

	@field:SerializedName("house")
	var house : ArrayList<QuessionnareModel>? = null
)

@Parcelize
data class QuessionnaireObjectModel(
	@field:SerializedName("education")
	var education : QuessionnareModel? = null,

	@field:SerializedName("family")
	var family : QuessionnareModel? = null,

	@field:SerializedName("job")
	var job : QuessionnareModel? = null,

	@field:SerializedName("income")
	var income : QuessionnareModel? = null,

	@field:SerializedName("house")
	var house : QuessionnareModel? = null
) : Parcelable