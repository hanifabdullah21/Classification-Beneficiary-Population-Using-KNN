package com.singpaulee.klasifikasipenerimabantuan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.singpaulee.klasifikasipenerimabantuan.LoadingInterface
import com.singpaulee.klasifikasipenerimabantuan.R
import com.singpaulee.klasifikasipenerimabantuan.algorythm.KNearestNeighbour
import com.singpaulee.klasifikasipenerimabantuan.connection.AppConfig
import com.singpaulee.klasifikasipenerimabantuan.connection.DataInterface
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireObjectModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_confusion_matrix.*
import kotlinx.android.synthetic.main.view_loading.*
import org.jetbrains.anko.toast

class ConfusionMatrixActivity : AppCompatActivity(), LoadingInterface, View.OnClickListener {
    

    private var TP: Int = 0
    private var FP: Int = 0
    private var TN: Int = 0
    private var FN: Int = 0

    private var listDataTraining : ArrayList<ClassificationDataModel>? = null
    private var listDataTest : ArrayList<ClassificationDataModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confusion_matrix)

        getListDataTest()

        vl_btn_reload.setOnClickListener(this)
        cma_btn_submit_k_value.setOnClickListener(this)
    }

    private fun countAccuration(
        dataTraining: ArrayList<ClassificationDataModel>?,
        dataTest: ArrayList<ClassificationDataModel>?,
        k: Int
    ) {
        resetValue()
        val knn = KNearestNeighbour()

        for (i in 0 until dataTest!!.size){

            val objekTes = QuessionnaireObjectModel(
                education = dataTest[i].education,
                family = dataTest[i].family,
                job = dataTest[i].job,
                income = dataTest[i].income,
                house = dataTest[i].house
            )
            val resultClassification = knn.doClassification(objekTes, dataTraining, k)

            /*Add to TP, FP, TN or FN*/
            when (dataTest[i].status) {
                "Menerima" -> {
                    when (resultClassification.status) {
                        "Menerima" -> TP++
                        "Tidak Menerima" -> FP++
                    }
                }
                "Tidak Menerima" -> {
                    when (resultClassification.status) {
                        "Tidak Menerima" -> TN++
                        "Menerima" -> FN++
                    }
                }
            }
        }

        showAccuration(dataTraining?.size, dataTest?.size)
    }

    private fun resetValue() {
        TN = 0
        FN = 0
        TP = 0
        FP = 0
    }

    private fun showAccuration(dataTrainingSize: Int?, dataTestSize: Int?) {
        cma_cv_result_accuration.visibility = View.VISIBLE

        cma_tv_size_data_training.text = dataTrainingSize.toString()
        cma_tv_size_data_test.text = dataTestSize.toString()

        cma_tv_k_value.text = cma_edt_k_value.text.toString()
        cma_tv_size_tp.text = TP.toString()
        cma_tv_size_fp.text = FP.toString()
        cma_tv_size_tn.text = TN.toString()
        cma_tv_size_fn.text = FN.toString()

        val accuracy: Double = ((TP.toDouble()+TN.toDouble())/(TP.toDouble()+TN.toDouble()+FP.toDouble()+FN.toDouble()))*100.toDouble()
        cma_tv_accuration.text = "$accuracy %"

        val precission: Double = (TP.toDouble()/(FP.toDouble()+TP.toDouble()))*100.toDouble()
        cma_tv_precission.text = "$precission %"

        val recall = (TP.toDouble()/(FN.toDouble()+TP.toDouble()))*100.toDouble()
        cma_tv_recall.text = "$recall %"
    }

    @SuppressLint("CheckResult")
    fun getListDataTest() {
        showLoading()
        val dataTraining = AppConfig.retrofitConfig(this)
            .create(DataInterface::class.java)
            .getListData("test")

        dataTraining.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                if (it.success as Boolean) {
                    Log.d("RESULT", it.toString())
                    if (it.data?.size == 0 || it.test?.size == 0) {
                        showErrorMessage("Data Training atau Data Tes Kosong")
                        vl_lottie_loading.setAnimation("lottie_empty_box.json")
                        vl_lottie_loading.playAnimation()
                    } else {
                        listDataTraining = it.data
                        listDataTest = it.test
                    }
                } else {
                    toast("failed")
                }
            }, {
                hideLoading()
                showErrorMessage(it.localizedMessage)
                toast(it.localizedMessage)
            })
    }

    override fun showLoading() {
        cma_loading.visibility = View.VISIBLE
        vl_textview.text = "Loading ..."
        vl_btn_reload.visibility = View.GONE

        cma_relativelayout.visibility = View.GONE
    }

    override fun hideLoading() {
        cma_loading.visibility = View.GONE
        cma_relativelayout.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {
        cma_loading.visibility = View.VISIBLE
        vl_textview.text = message
        vl_btn_reload.visibility = View.VISIBLE

        cma_relativelayout.visibility = View.GONE
    }

    override fun onClick(p0: View?) {
        when(p0){
            vl_btn_reload -> getListDataTest()
            cma_btn_submit_k_value -> {
                if (cma_edt_k_value.text.isBlank() || cma_edt_k_value.text.isEmpty()){
                    cma_edt_k_value.error = "Harap masukan nilai k"
                    cma_edt_k_value.requestFocus()
                    return
                }
                countAccuration(listDataTraining, listDataTest, cma_edt_k_value.text.toString().toInt())
            }
        }
    }
}
