package edu.washington.ieand.quizdroid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
//import android.widget.Toast

class ControllerActivity : AppCompatActivity(), TopicFragment.OnBeginBtnClickedListener,
        QuizFragment.OnQuizSubmitBtnClickListener, AnswerFragment.OnContinueBtnClickedListener {

    private lateinit var currTopicName: String
    private lateinit var currTopicData: TopicData
    private var currQuestionNumber = 0
    private var numCorrectAnswers = 0
    private val instance = QuizApp.getSingletonInstance()
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        currTopicName = intent.extras[TOPIC_NAME] as String
        currTopicData = instance.repository.getTopic(currTopicName)

        var fragment: Fragment? = null

        if (currQuestionNumber == 0) {
            fragment = TopicFragment.newInstance(currTopicName, currTopicData)
        }

        if (null != fragment) {
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment)
            ft.commit()
        }
    }

    override fun onBeginBtnClicked() {
        currQuestionNumber++

        val fragment: Fragment? = QuizFragment.newInstance(currQuestionNumber.toString(), currTopicData)
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commit()
    }

    override fun onQuizSubmitBtnClicked(chosenAnswer: String?, correctAnswer: String?) {
        if (chosenAnswer == correctAnswer) {
            numCorrectAnswers++
        }

        val fragment: Fragment? = AnswerFragment.newInstance(chosenAnswer as String, correctAnswer as String,
                numCorrectAnswers.toString(), currQuestionNumber.toString(), currTopicData)
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.commit()
    }

    override fun onContinueBtnClicked() {
        val totalNumQuestions = currTopicData.questions?.count() // currTopicData.getQuestions().count().toString()
        if (currQuestionNumber.toString() == totalNumQuestions.toString()) {
            val intent = Intent(this, MainV2Activity::class.java)
            startActivity(intent)
        } else {
            onBeginBtnClicked()
        }
    }

    companion object {
        const val TOPIC_NAME = "uw.ischool.edu.ieand.QuizDroid.ControllerActivity.TOPIC_NAME"
    }
}
