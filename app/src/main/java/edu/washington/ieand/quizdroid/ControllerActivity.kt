package edu.washington.ieand.quizdroid

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast

class ControllerActivity : AppCompatActivity(), TopicFragment.OnBeginBtnPressedListener,
        QuizFragment.OnQuizSubmitBtnClickListener, AnswerFragment.OnContinueBtnClickedListener {

    private lateinit var currTopic: String
    private lateinit var currTopicData: HashMap<String, String>
    private var currQuestionNumber = 0
    private var numCorrectAnswers = 0
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        currTopic = intent.extras[TOPIC_NAME] as String

        var fragment: Fragment? = null

        currTopicData = when(intent.extras[TOPIC_NAME] as String) {
            "Math" -> topic1
            "Physics" -> topic2
            "Marvel Super Heroes" -> topic3
            "Greek Mythology" -> topic4
            else -> {
                topic1
            }
        }

        if (currQuestionNumber == 0) {
            fragment = TopicFragment.newInstance(currTopicData["name"] as String, currTopicData["description"] as String, currTopicData)
        }

        if (null != fragment) {
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment)
            ft.commit()
        }
    }

    override fun onBeginBtnPressed() {
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
        if (currQuestionNumber.toString() == currTopicData["numQuestions"]) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            onBeginBtnPressed()
        }
    }

    companion object {
        const val TOPIC_NAME = "uw.ischool.edu.ieand.QuizDroid.ControllerActivity.TOPIC_NAME"

        private val topic1 = hashMapOf(
                "name" to "Math",
                "description" to "A series of questions that will test your mathematical intuition.",
                "numQuestions" to "4",
                "Q1" to "Sine, cosine, and what?",
                "Q1_A1" to "Quotient",
                "Q1_A2" to "Tangent",
                "Q1_A3" to "Agent",
                "Q1_A4" to "Potient",
                "Q1_correctAnswer" to "Tangent",
                "Q2" to "36 * 7?",
                "Q2_A1" to "256",
                "Q2_A2" to "264",
                "Q2_A3" to "246",
                "Q2_A4" to "252",
                "Q2_correctAnswer" to "252",
                "Q3" to "Solve 9 + 9 / 3",
                "Q3_A1" to "6",
                "Q3_A2" to "12",
                "Q3_A3" to "9",
                "Q3_A4" to "15",
                "Q3_correctAnswer" to "12",
                "Q4" to "2^8?",
                "Q4_A1" to "128",
                "Q4_A2" to "256",
                "Q4_A3" to "512",
                "Q4_A4" to "1024",
                "Q4_correctAnswer" to "256"
        )
        private val topic2 = hashMapOf(
                "name" to "Physics",
                "description" to "How well do you and gravity get along? Let's find out!",
                "numQuestions" to "4",
                "Q1" to "What was Newton's first law?",
                "Q1_A1" to "The law of motion",
                "Q1_A2" to "The law of gravity",
                "Q1_A3" to "The law of movement",
                "Q1_A4" to "The law of order",
                "Q1_correctAnswer" to "The law of motion",
                "Q2" to "What is the force that opposes the relative motion of two bodies that are in contact?",
                "Q2_A1" to "Normal Force",
                "Q2_A2" to "Magnetism",
                "Q2_A3" to "Friction",
                "Q2_A4" to "Gravity",
                "Q2_correctAnswer" to "Friction",
                "Q3" to "The discovery of which law provoked the surprised cry \"Eureka!\"?",
                "Q3_A1" to "The law of Gravity",
                "Q3_A2" to "Newton's 4th Law",
                "Q3_A3" to "The law of Relativity",
                "Q3_A4" to "Archimedes Principle",
                "Q3_correctAnswer" to "Archimedes Principle",
                "Q4" to "Magnets — How do they work?",
                "Q4_A1" to "Magic",
                "Q4_A2" to "The interaction between electrons of opposite charge",
                "Q4_A3" to "Friction",
                "Q4_A4" to "Electricity",
                "Q4_correctAnswer" to "The interaction between electrons of opposite charge"
        )
        private val topic3 = hashMapOf(
                "name" to "Marvel Super Heroes",
                "description" to "Feel like you could assemble all the Avengers? Let's see what kind of Nick Fury you make.",
                "numQuestions" to "4",
                "Q1" to "What powers Iron Man's suit?",
                "Q1_A1" to "He was bit by a robot",
                "Q1_A2" to "Arc Reactor",
                "Q1_A3" to "Unobtainium",
                "Q1_A4" to "Russian Titanium",
                "Q1_correctAnswer" to "Arc Reactor",
                "Q2" to "In the comics, who else can lift Thor's hammer?",
                "Q2_A1" to "Wade Wilson",
                "Q2_A2" to "Miles Morales",
                "Q2_A3" to "Stephen Strange",
                "Q2_A4" to "Natasha Romanoff",
                "Q2_correctAnswer" to "Natasha Romanoff",
                "Q3" to "How many Infinity Stones are there?",
                "Q3_A1" to "4",
                "Q3_A2" to "5",
                "Q3_A3" to "6",
                "Q3_A4" to "7",
                "Q3_correctAnswer" to "6",
                "Q4" to "How many movies has Stan Lee had a cameo in?",
                "Q4_A1" to "44",
                "Q4_A2" to "37",
                "Q4_A3" to "29",
                "Q4_A4" to "53",
                "Q4_correctAnswer" to "44"
        )
        private val topic4 = hashMapOf(
                "name" to "Greek Mythology",
                "description" to "How well do you know the classic of Greece and Rome? This category will stress your knowledge of the ancient world.",
                "numQuestions" to "4",
                "Q1" to "What was Odysseus' wife's name?",
                "Q1_A1" to "Persephone",
                "Q1_A2" to "Penelope",
                "Q1_A3" to "Circe",
                "Q1_A4" to "Thetis",
                "Q1_correctAnswer" to "Penelope",
                "Q2" to "Which mythical monster did Theseus of Athens slay?",
                "Q2_A1" to "Medusa",
                "Q2_A2" to "Scylla",
                "Q2_A3" to "Hydra",
                "Q2_A4" to "Minotaur",
                "Q2_correctAnswer" to "Minotaur",
                "Q3" to "Who was the father of Kronos?",
                "Q3_A1" to "Zeus",
                "Q3_A2" to "Ouranos",
                "Q3_A3" to "Ares",
                "Q3_A4" to "Gaia",
                "Q3_correctAnswer" to "Ouranos",
                "Q4" to "What was the \"Works and Days?\"",
                "Q4_A1" to "A story of the world\\'s creation",
                "Q4_A2" to "A recounting of Hercules' deeds",
                "Q4_A3" to "An all-year farming advice guidebook",
                "Q4_A4" to "A calendar for Athenian statesmen",
                "Q4_correctAnswer" to "An all-year farming advice guidebook"
        )
    }
}
