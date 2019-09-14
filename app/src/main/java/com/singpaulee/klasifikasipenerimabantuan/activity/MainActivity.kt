package com.singpaulee.klasifikasipenerimabantuan.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.singpaulee.klasifikasipenerimabantuan.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_cv_classification.setOnClickListener(this)
        main_cv_list_classification.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            main_cv_classification -> {
                startActivity(intentFor<QuestionnaireActivity>())
            }
            main_cv_list_classification -> {
                startActivity(intentFor<ListClassificationActivity>())
            }
        }
    }
}
