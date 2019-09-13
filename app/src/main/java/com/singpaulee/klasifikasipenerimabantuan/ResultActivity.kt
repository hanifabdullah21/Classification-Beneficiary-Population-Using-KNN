package com.singpaulee.klasifikasipenerimabantuan

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.singpaulee.klasifikasipenerimabantuan.algorythm.KNearestNeighbour
import com.singpaulee.klasifikasipenerimabantuan.connection.AppConfig
import com.singpaulee.klasifikasipenerimabantuan.connection.DataInterface
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireObjectModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_questionnaire.*
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.toast

class ResultActivity : AppCompatActivity(), LoadingInterface {
    val TAG = "RESULT"

    var qAnswerModel : QuessionnaireObjectModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        qAnswerModel = intent.getParcelableExtra("answer")
        Log.d(TAG, "Questionnaire Answer $qAnswerModel")

        getListDataTraining()
    }

    @SuppressLint("CheckResult")
    fun getListDataTraining(){
        showLoading()
        val dataTraining = AppConfig.retrofitConfig(this)
            .create<DataInterface>(DataInterface::class.java)
            .getListData("list")

        dataTraining.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                if (it.success as Boolean) {
                    Log.d("RESULT", it.toString())
                    executeKNN(it.data)
                } else {
                    toast("failed")
                }
            }, {
                showLoading()
                toast(it.localizedMessage)
            })
    }

    private fun executeKNN(data: ArrayList<ClassificationDataModel>?) {
        val knn = KNearestNeighbour()

        val resultClassification = knn.doClassification(qAnswerModel, data, 3)

        Log.d("RESULT", resultClassification.status)
        setContent(resultClassification)
    }

    private fun setContent(resultClassification: ClassificationDataModel) {
        ra_tv_name.text = "Masih Belum ada nama"
        ra_tv_education.text = resultClassification.education?.variable.toString()
        ra_tv_family.text = resultClassification.family?.variable.toString()
        ra_tv_job.text = resultClassification.job?.variable.toString()
        ra_tv_house.text = resultClassification.house?.variable.toString()
        ra_tv_income.text = resultClassification.income?.variable.toString()
        ra_tv_status.text = resultClassification.status.toString()
    }

    override fun showLoading() {
        ra_viewgroup_content.visibility = View.GONE
        ra_viewgroup_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        ra_viewgroup_content.visibility = View.VISIBLE
        ra_viewgroup_loading.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {

    }
}
