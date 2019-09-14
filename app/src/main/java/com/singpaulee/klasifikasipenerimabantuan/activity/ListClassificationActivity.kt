package com.singpaulee.klasifikasipenerimabantuan.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.singpaulee.klasifikasipenerimabantuan.LoadingInterface
import com.singpaulee.klasifikasipenerimabantuan.R
import com.singpaulee.klasifikasipenerimabantuan.adapter.ClassificationAdapter
import com.singpaulee.klasifikasipenerimabantuan.connection.AppConfig
import com.singpaulee.klasifikasipenerimabantuan.connection.DataInterface
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list_classification.*
import kotlinx.android.synthetic.main.view_loading.*
import org.jetbrains.anko.toast

class ListClassificationActivity : AppCompatActivity(), LoadingInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_classification)

        getListDataTraining()
    }

    @SuppressLint("CheckResult")
    fun getListDataTraining() {
        showLoading()
        val dataTraining = AppConfig.retrofitConfig(this)
            .create(DataInterface::class.java)
            .getListData("classification")

        dataTraining.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                if (it.success as Boolean) {
                    Log.d("RESULT", it.toString())
                    if (it.data!!.isEmpty()){
                        showEmptyMessage()
                    }else{
                        showRecyclerView(it.data)
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

    private fun showRecyclerView(data: ArrayList<ClassificationDataModel>) {
        lca_rv_classification.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = ClassificationAdapter(this, data)
        lca_rv_classification.adapter = adapter
    }

    private fun showEmptyMessage() {
        lca_rv_classification.visibility = View.GONE
        lca_view_loading.visibility = View.VISIBLE

        vl_lottie_loading.setAnimation("lottie_empty_box.json")
        vl_lottie_loading.playAnimation()
        vl_textview.text = "Data Klasifikasi Kosong"
        vl_btn_reload.visibility = View.GONE
    }

    override fun showLoading() {
        lca_rv_classification.visibility = View.GONE
        lca_view_loading.visibility = View.VISIBLE
        vl_textview.text = "Loading ..."
        vl_btn_reload.visibility = View.GONE
    }

    override fun hideLoading() {
        lca_rv_classification.visibility = View.VISIBLE
        lca_view_loading.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        lca_rv_classification.visibility = View.GONE
        lca_view_loading.visibility = View.VISIBLE
        vl_textview.text = message
        vl_btn_reload.visibility = View.VISIBLE
    }
}
