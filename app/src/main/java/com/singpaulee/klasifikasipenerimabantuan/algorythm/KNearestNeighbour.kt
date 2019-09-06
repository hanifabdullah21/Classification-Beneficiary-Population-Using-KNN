package com.singpaulee.klasifikasipenerimabantuan.algorythm

import android.util.Log
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireObjectModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnareModel
import kotlin.math.pow
import kotlin.math.sqrt

class KNearestNeighbour() {

    val TAG = "KNEARESTNEIGHBOUR"

    fun doClassification(
        dataClassification: QuessionnaireObjectModel?,
        listData: ArrayList<ClassificationDataModel>?,
        k: Int
    ): ClassificationDataModel {
        val listAfterCountDistance = countDistance(dataClassification, listData)
        Log.d(TAG, "List After Count Distance \n $listAfterCountDistance")

        val listAfterSorting = sortListByDistance(listAfterCountDistance)
        Log.d(TAG, "List After Sorting \n $listAfterSorting")

        val result = getResult(listAfterSorting, k)
        Log.d(TAG, "Result $result")

        return ClassificationDataModel(
            education = QuessionnareModel(
                variable = dataClassification?.education?.variable,
                weight = dataClassification?.education?.weight
            ),
            family = QuessionnareModel(
                variable = dataClassification?.family?.variable,
                weight = dataClassification?.family?.weight
            ),
            job = QuessionnareModel(
                variable = dataClassification?.job?.variable,
                weight = dataClassification?.job?.weight
            ),
            income = QuessionnareModel(
                variable = dataClassification?.income?.variable,
                weight = dataClassification?.income?.weight
            ),
            house = QuessionnareModel(
                variable = dataClassification?.house?.variable,
                weight = dataClassification?.house?.weight
            ),
            status = result

        )
    }

    private fun countDistance(
        dataClassification: QuessionnaireObjectModel?, listData: ArrayList<ClassificationDataModel>?
    ): ArrayList<ClassificationDataModel> {

        //TODO Count distance using euclidian distance algorythm
        for (i in listData?.indices!!) {

            val diffEducation =
                (dataClassification?.education?.weight!! - listData[i].education?.weight!!).toDouble().pow(2)
            val diffFamily =
                (dataClassification.family?.weight!! - listData[i].family?.weight!!).toDouble().pow(2)
            val diffJob =
                (dataClassification.job?.weight!! - listData[i].job?.weight!!).toDouble().pow(2)
            val diffIncome =
                (dataClassification.income?.weight!! - listData[i].income?.weight!!).toDouble().pow(2)
            val diffHouse =
                (dataClassification.house?.weight!! - listData[i].house?.weight!!).toDouble().pow(2)

            val distance =
                sqrt(diffEducation + diffFamily + diffJob + diffIncome + diffHouse)

            Log.d(TAG, "Count Distance $i ${listData.get(i).name} ${listData.get(i).distance}")

            listData[i].distance = distance
        }
        return listData
    }


    private fun sortListByDistance(listData: ArrayList<ClassificationDataModel>): List<ClassificationDataModel> {
        return listData.sortedBy { it.distance }
    }

    private fun getResult(listAfterSorting: List<ClassificationDataModel>, k: Int): String {
        var countReceive = 0
        var countUnReceive = 0
        for (i in 0 until k) {
            if (listAfterSorting[i].status.equals("Menerima")) countReceive++ else countUnReceive++
        }
        return if (countReceive > countUnReceive) "Menerima" else "Tidak Menerima"
    }
}