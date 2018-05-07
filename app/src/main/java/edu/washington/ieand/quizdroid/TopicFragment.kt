package edu.washington.ieand.quizdroid

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

private const val ARG_PARAM1 = "currTopicName"

class TopicFragment : Fragment() {
    private var currTopicName: String? = null
    private var _quizData: TopicData? = null
    private var listener: OnBeginBtnClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currTopicName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the TextView content
        val topicHeading: TextView = view.findViewById(R.id.textView_topicFragment_topicHeading)
        topicHeading.text = currTopicName
        val topicDescription: TextView = view.findViewById(R.id.textView_topicFragment_topicDescription)
        topicDescription.text = _quizData?.desc ?: "TOPIC_DESCRIPTION"
//        topicDescription.text = _quizData?.topicDescription ?: "TOPIC_DESCRIPTION"
        val numQuestions: TextView = view.findViewById(R.id.textView_topicFragment_numQuestions)
        val numQuestionsAmt: String = _quizData?.questions?.count().toString()
//        val numQuestionsAmt: String = _quizData?.getQuestions()?.count().toString()
        val numQuestionsText = "Number of Questions: $numQuestionsAmt"
        numQuestions.text = numQuestionsText

        // Set button listener
        val beginBtn: Button = view.findViewById(R.id.button_topicFragment_startQuiz)
        beginBtn.setOnClickListener {
            listener?.onBeginBtnClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBeginBtnClickedListener) {
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

    interface OnBeginBtnClickedListener {
        fun onBeginBtnClicked()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, quizData: TopicData) =
            TopicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
                _quizData = quizData
            }

        const val TAG = "TopicFragment"
    }
}
