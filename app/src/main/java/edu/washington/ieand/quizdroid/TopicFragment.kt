package edu.washington.ieand.quizdroid

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TopicFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TopicFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TopicFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _quizData: HashMap<String, String>? = null
    private var listener: OnBeginBtnPressedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_topic, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the TextView content
        val topicHeading: TextView = view.findViewById(R.id.textView_topicFragment_topicHeading)
        topicHeading.text = param1
        val topicDescription: TextView = view.findViewById(R.id.textView_topicFragment_topicDescription)
        topicDescription.text = param2
        val numQuestions: TextView = view.findViewById(R.id.textView_topicFragment_numQuestions)
        val numQuestionsText = "Number of Questions: " + (_quizData?.get("numQuestions") ?: "who knows!")
        numQuestions.text = numQuestionsText
        // Set button listener
        val beginBtn: Button = view.findViewById(R.id.button_topicFragment_startQuiz)
        beginBtn.setOnClickListener {
            listener?.onBeginBtnPressed()
        }

//        Log.i(TAG, _quizData?.get("name"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBeginBtnPressedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnBeginBtnPressedListener {
        // TODO: Update argument type and name
        fun onBeginBtnPressed()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TopicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, quizData: HashMap<String, String>) =
            TopicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
                _quizData = quizData
            }


        const val TAG = "TopicFragment"

//        const val TOPIC = "uw.ischool.edu.TopicFragment.TOPIC"
    }
}
