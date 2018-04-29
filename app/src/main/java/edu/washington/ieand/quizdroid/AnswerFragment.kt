package edu.washington.ieand.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "givenAnswer"
private const val ARG_PARAM2 = "correctAnswer"
private const val ARG_PARAM3 = "totalCorrectAnswers"
private const val ARG_PARAM4 = "currQuestionNum"

class AnswerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var givenAnswer: String? = null
    private var correctAnswer: String? = null
    private var totalCorrectAnswers: String? = null
    private var currQuestionNum: String? = null
    private var _quizData: HashMap<String, String>? = null
    private var listener: OnContinueBtnClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            givenAnswer = it.getString(ARG_PARAM1)
            correctAnswer = it.getString(ARG_PARAM2)
            totalCorrectAnswers = it.getString(ARG_PARAM3)
            currQuestionNum = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the TextView text content
        val givenAnswerTextView: TextView = view.findViewById(R.id.textView_answerFragment_givenAnswer)
        val givenAnswerText = "Your answer: $givenAnswer"
        givenAnswerTextView.text = givenAnswerText
        val correctAnswerTextView: TextView = view.findViewById(R.id.textView_answerFragment_correctAnswer)
        val correctAnswerText = "Correct answer: $correctAnswer"
        correctAnswerTextView.text = correctAnswerText
        val currScoreTextView: TextView = view.findViewById(R.id.textView_answerFragment_score)
        val currScoreText = "You have $totalCorrectAnswers of $currQuestionNum correct."
        currScoreTextView.text = currScoreText

        // Change text to 'finish' if quiz finished
        val continueBtn: Button = view.findViewById(R.id.button_answerFragment_continue)
        val totalNumQuestions: String? = _quizData?.get("numQuestions")
        if (currQuestionNum == totalNumQuestions) {
            continueBtn.text = getString(R.string.button_answer_finishQuiz)
        }
        // Set button listener
        continueBtn.setOnClickListener {
            listener?.onContinueBtnClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContinueBtnClickedListener) {
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

    interface OnContinueBtnClickedListener {
        // TODO: Update argument type and name
        fun onContinueBtnClicked()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(givenAnswer: String, correctAnswer: String, totalCorrectAnswers: String,
                        currQuestionNum: String, quizData: HashMap<String, String>) =
                AnswerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, givenAnswer)
                        putString(ARG_PARAM2, correctAnswer)
                        putString(ARG_PARAM3, totalCorrectAnswers)
                        putString(ARG_PARAM4, currQuestionNum)
                    }

                    _quizData = quizData
                }
    }
}
