package com.singpaulee.klasifikasipenerimabantuan.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.singpaulee.klasifikasipenerimabantuan.R
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import kotlinx.android.synthetic.main.activity_detail_classification.*

class DetailClassificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_classification)

        val dataClassification = intent.getParcelableExtra<ClassificationDataModel>("model")

        dca_tv_name.text = dataClassification.name
        dca_tv_education.text = dataClassification.education?.variable
        dca_tv_family.text = dataClassification.family?.variable
        dca_tv_job.text = dataClassification.job?.variable
        dca_tv_income.text = dataClassification.income?.variable
        dca_tv_house.text = dataClassification.house?.variable
        dca_tv_status.text = dataClassification.status
    }
}
