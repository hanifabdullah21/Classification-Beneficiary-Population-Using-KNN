package com.singpaulee.klasifikasipenerimabantuan.activity

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.singpaulee.klasifikasipenerimabantuan.LoadingInterface
import com.singpaulee.klasifikasipenerimabantuan.R
import com.singpaulee.klasifikasipenerimabantuan.algorythm.KNearestNeighbour
import com.singpaulee.klasifikasipenerimabantuan.connection.AppConfig
import com.singpaulee.klasifikasipenerimabantuan.connection.DataInterface
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireObjectModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.view_loading.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.toast

class ResultActivity : AppCompatActivity(), LoadingInterface, View.OnClickListener {

    val TAG = "RESULT"
    var STAGE: Int = 0

    var qAnswerModel: QuessionnaireObjectModel? = null
    var resultClassification: ClassificationDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        qAnswerModel = intent.getParcelableExtra("answer")
        Log.d(TAG, "Questionnaire Answer $qAnswerModel")

        getListDataTraining()

        ra_btn_add_to_db.setOnClickListener(this)
        ra_btn_back_to_main_menu.setOnClickListener(this)
        vl_btn_reload.setOnClickListener(this)

    }

    @SuppressLint("CheckResult")
    fun getListDataTraining() {
        STAGE = 1
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
                    showErrorMessage(it.message.toString())
                }
            }, {
                hideLoading()
                showErrorMessage(it.localizedMessage)
                toast(it.localizedMessage)
            })
    }

    @SuppressLint("CheckResult")
    fun postDataClassification(){
        STAGE=2
        showLoading()
        val postData = AppConfig.retrofitConfig(this)
            .create(DataInterface::class.java)
            .postClassificationData(
                intent?.getStringExtra("name").toString(),
                resultClassification?.education?.weight,
                resultClassification?.family?.weight,
                resultClassification?.job?.weight,
                resultClassification?.income?.weight,
                resultClassification?.house?.weight,
                resultClassification?.status
            )

        postData.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                if (it?.success as Boolean){
                    ra_btn_add_to_db.text = "Berhasil ditambahkan"
                    ra_btn_add_to_db.backgroundColor = Color.GRAY
                    ra_btn_add_to_db.isEnabled = false
                }else{
                    showErrorMessage(it.message.toString())
                }
            }, {
                hideLoading()
                showErrorMessage(it.localizedMessage)
            })
    }

    private fun executeKNN(data: ArrayList<ClassificationDataModel>?) {
        val knn = KNearestNeighbour()

        resultClassification = knn.doClassification(qAnswerModel, data, 3)

        Log.d("RESULT", resultClassification?.status)
        setContent(resultClassification)
    }

    private fun setContent(resultClassification: ClassificationDataModel?) {
        ra_tv_name.text = intent.getStringExtra("name")
        ra_tv_education.text = resultClassification?.education?.variable.toString()
        ra_tv_family.text = resultClassification?.family?.variable.toString()
        ra_tv_job.text = resultClassification?.job?.variable.toString()
        ra_tv_house.text = resultClassification?.house?.variable.toString()
        ra_tv_income.text = resultClassification?.income?.variable.toString()
        ra_tv_status.text = resultClassification?.status.toString()
    }

    override fun showLoading() {
        ra_viewgroup_content.visibility = View.GONE
        ra_viewgroup_loading.visibility = View.VISIBLE

        vl_textview.text = "Loading..."
        vl_btn_reload.visibility = View.GONE
    }

    override fun hideLoading() {
        ra_viewgroup_content.visibility = View.VISIBLE
        ra_viewgroup_loading.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        ra_viewgroup_content.visibility = View.GONE
        ra_viewgroup_loading.visibility = View.VISIBLE
        vl_textview.text = message
        vl_btn_reload.visibility = View.VISIBLE
    }

    override fun onClick(p0: View?) {
        when(p0){
            ra_btn_add_to_db -> postDataClassification()
            ra_btn_back_to_main_menu -> onBackPressed()
            vl_btn_reload -> {
                when(STAGE){
                    1 -> getListDataTraining()
                    2 -> postDataClassification()
                }
            }
        }
    }
}
