package edu.washington.ieand.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView


private const val ARG_PARAM1 = "currQuestionNumber"

class QuizFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var currQuestionNumber: String? = null
    private var listener: OnQuizSubmitBtnClickListener? = null
    private var _quizData: HashMap<String, String>? = null
    private var _selectedAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currQuestionNumber = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the TextView text content
        val quizQuestion: TextView = view.findViewById(R.id.textView_quiz_question)
        val currQuestionKey = "Q$currQuestionNumber"
        // Set the RadioButton text content
        quizQuestion.text = _quizData?.get(currQuestionKey) ?: "Oops"
        val quizAnswer1: RadioButton = view.findViewById(R.id.radioButton_quiz_answer1)
        val quizAnswer1Key: String = currQuestionKey + "_A1"
        quizAnswer1.text = _quizData?.get(quizAnswer1Key) ?: "Oops"
        val quizAnswer2: RadioButton = view.findViewById(R.id.radioButton_quiz_answer2)
        val quizAnswer2Key: String = currQuestionKey + "_A2"
        quizAnswer2.text = _quizData?.get(quizAnswer2Key) ?: "Oops"
        val quizAnswer3: RadioButton = view.findViewById(R.id.radioButton_quiz_answer3)
        val quizAnswer3Key: String = currQuestionKey + "_A3"
        quizAnswer3.text = _quizData?.get(quizAnswer3Key) ?: "Oops"
        val quizAnswer4: RadioButton = view.findViewById(R.id.radioButton_quiz_answer4)
        val quizAnswer4Key: String = currQuestionKey + "_A4"
        quizAnswer4.text = _quizData?.get(quizAnswer4Key) ?: "Oops"
        // Disable submit button until answer selected
        val submitBtn: Button = view.findViewById(R.id.button_quiz_submit)
        submitBtn.isEnabled = false
        // Keep track of selected answer, enable submit button
        val answerWrapper: RadioGroup = view.findViewById(R.id.radioGroup_quiz_answerWrapper)
        answerWrapper.setOnCheckedChangeListener { _, checkedId ->
            _selectedAnswer = view.findViewById<RadioButton>(checkedId).text as String
            submitBtn.isEnabled = true
            Log.i(TAG, _selectedAnswer)
        }

        // Set button
        val quizCorrectAnswerKey: String = currQuestionKey + "_correctAnswer"
        val quizCorrectAnswer = _quizData?.get(quizCorrectAnswerKey)
        submitBtn.setOnClickListener {
            listener?.onQuizSubmitBtnClicked(_selectedAnswer, quizCorrectAnswer)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnQuizSubmitBtnClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        _quizData = null
    }

    interface OnQuizSubmitBtnClickListener {
        fun onQuizSubmitBtnClicked(chosenAnswer: String?, correctAnswer: String?)
    }

    companion object {
        @JvmStatic
        fun newInstance(currQuestionNumber: String, quizData: HashMap<String, String>) =
                QuizFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, currQuestionNumber)
                    }

                    _quizData = quizData
                }

        const val TAG = "QuizFragment"
    }
}
