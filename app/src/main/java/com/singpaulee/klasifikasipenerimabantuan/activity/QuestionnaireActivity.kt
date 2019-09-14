package com.singpaulee.klasifikasipenerimabantuan.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.singpaulee.klasifikasipenerimabantuan.connection.AppConfig
import com.singpaulee.klasifikasipenerimabantuan.connection.DataInterface
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireListModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnaireObjectModel
import com.singpaulee.klasifikasipenerimabantuan.model.QuessionnareModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_questionnaire.*
import kotlinx.android.synthetic.main.view_loading.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import com.singpaulee.klasifikasipenerimabantuan.LoadingInterface
import com.singpaulee.klasifikasipenerimabantuan.R


class QuestionnaireActivity : AppCompatActivity(), View.OnClickListener,
    LoadingInterface {



    val TAG = "QuestionnaireAct"

    var indexQuestionnaire = 0                              //Index pertanyaan yang tampil
    var dataQuestionnaire: QuessionnaireListModel? = null   //objek untuk menampung data dari api
    var selectedAnswer: QuessionnaireObjectModel? = null

    var name : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        selectedAnswer = QuessionnaireObjectModel()

        qa_btn_next.setOnClickListener(this)
        qa_btn_submit_name.setOnClickListener(this)
        vl_btn_reload.setOnClickListener(this)

        getQuestionnaire()
    }

    override fun onClick(v: View?) {
        when (v) {
            qa_btn_next -> {
                if (qa_radiogroup.checkedRadioButtonId != -1){
                    getSelectedRadioButton()
                    qa_radiogroup.removeAllViews()
                    qa_radiogroup.clearCheck()
                    indexQuestionnaire++
                    showQuestionnaire()
                }else{
                    toast("Silahkan pilih jawaban dulu.")
                }
            }
            vl_btn_reload -> {
                getQuestionnaire()
            }
            qa_btn_submit_name -> {
                if(qa_edt_name.text.isBlank() || qa_edt_name.text.isEmpty()){
                    qa_edt_name.error = "Silahkan isi nama dahulu"
                    qa_edt_name.requestFocus()
                }else{
                    hideKeyboard(this)
                    name = qa_edt_name.text.toString()
                    qa_cv_name.visibility = View.GONE
                    qa_rl_questionnaire.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getSelectedRadioButton() {
        val radiobutton = findViewById<RadioButton>(qa_radiogroup.checkedRadioButtonId)
        val answer = radiobutton.text.toString()
        when (indexQuestionnaire) {
            0 -> {
                selectedAnswer?.education = searchValue(answer, dataQuestionnaire?.education)
            }
            1 -> {
                selectedAnswer?.family = searchValue(answer, dataQuestionnaire?.family)
            }
            2 -> {
                selectedAnswer?.job = searchValue(answer, dataQuestionnaire?.job)
            }
            3 -> {
                selectedAnswer?.income = searchValue(answer, dataQuestionnaire?.income)
            }
            4 -> {
                selectedAnswer?.house = searchValue(answer, dataQuestionnaire?.house)
            }
            else -> {

            }
        }
    }

    /** Mencari objek dari daftar quessionaire berdasarkan jawaban yang dipilih
     * sesuai dengan pertanyannya
     *
     * */
    private fun searchValue(answer: String?, qList: ArrayList<QuessionnareModel>?): QuessionnareModel? {
        var qModel = QuessionnareModel()
        Log.d(TAG, "Selected Answer $answer")
        for (q in qList!!) {
            if (q.variable.toString() == answer) {
                qModel = q
            }
        }
        Log.d(TAG, "Result Q Model $qModel")
        return qModel
    }

    @SuppressLint("CheckResult")
    fun getQuestionnaire() {
        showLoading()
        val questionnaire = AppConfig.retrofitConfig(this)
            .create(DataInterface::class.java)
            .getQuestionnaire("questionnaire")

        questionnaire.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
                if (it.success as Boolean) {
                    Log.d("QUESTIONNAIRE", it.toString())
                    dataQuestionnaire = it.data
                    showQuestionnaire()
                } else {
                    toast("failed")
                    showErrorMessage("Kesalahan !!")
                }
            }, {
                hideLoading()
                showErrorMessage(it.localizedMessage)
                toast(it.localizedMessage)
            })
    }

    private fun showQuestionnaire() {
        when (indexQuestionnaire) {
            0 -> {
                //Education
                val title = "Pendidikan Terakhir"
                Log.d(TAG, "Show Questionnaire $title")
                qa_tv_title.text = title
                val listQEducation = dataQuestionnaire?.education
                setRadioButton(listQEducation)
            }
            1 -> {
                val title = "Jumlah Tanggungan"
                Log.d(TAG, "Show Questionnaire $title")
                qa_tv_title.text = title
                val listQFamily = dataQuestionnaire?.family
                setRadioButton(listQFamily)
            }
            2 -> {
                //Job
                val title = "Pekerjaan"
                Log.d(TAG, "Show Questionnaire $title")
                qa_tv_title.text = title
                val listQJob = dataQuestionnaire?.job
                setRadioButton(listQJob)
            }
            3 -> {
                //Income
                val title = "Jumlah Pendapatan"
                Log.d(TAG, "Show Questionnaire $title")
                qa_tv_title.text = title
                val listQIncome = dataQuestionnaire?.income
                setRadioButton(listQIncome)
            }
            4 -> {
                //House Condition
                val title = "Kondisi Rumah"
                Log.d(TAG, "Show Questionnaire $title")
                qa_tv_title.text = title
                val listQHouse = dataQuestionnaire?.house
                setRadioButton(listQHouse)
            }
            else -> {
                moveToResult()
            }
        }
    }

    private fun setRadioButton(listQ: ArrayList<QuessionnareModel>?) {
        for (i in listQ!!.indices) {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = listQ[i].variable.toString()
            qa_radiogroup.addView(radioButton)
        }
    }

    private fun moveToResult(){
        finish()
        startActivity(intentFor<ResultActivity>("answer" to selectedAnswer, "name" to name))
    }

    override fun showLoading() {
        //Show loading view and hide questionnaire and carview name
        qa_rl_questionnaire.visibility = View.GONE
        qa_cv_name.visibility = View.GONE

        qa_loading.visibility = View.VISIBLE
        vl_textview.text = "Loading ..."
        vl_btn_reload.visibility = View.GONE
    }

    override fun hideLoading() {
        //Show cardview name and hide loading view and questionnaire
        qa_rl_questionnaire.visibility = View.GONE
        qa_cv_name.visibility = View.VISIBLE

        qa_loading.visibility = View.GONE
        vl_btn_reload.visibility = View.GONE
    }

    override fun showErrorMessage(message: String) {
        //Show loading view and error message , and hide cardview name and questionnaire
        qa_rl_questionnaire.visibility = View.GONE
        qa_cv_name.visibility = View.GONE

        qa_loading.visibility = View.VISIBLE
        vl_textview.text = message
        vl_btn_reload.visibility = View.VISIBLE
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
