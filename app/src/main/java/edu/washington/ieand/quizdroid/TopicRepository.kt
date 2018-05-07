package edu.washington.ieand.quizdroid

import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class TopicRepository : ITopicRepository {
//    private var _topics = mutableListOf<Topic>()
    private var _topicData = mutableListOf<TopicData>()

    override fun getTopic(topicName: String): TopicData {
        val targetTopic = _topicData.filter { it.title == topicName }

        return targetTopic.first()
    }

    override fun getTopics(): List<TopicData> { // List<Topic> {
        return _topicData.toList()
    }

    override fun getDefaultUrl(): String {
        return "http://tednewardsandbox.site44.com/questions.json"
    }

    fun buildTopics(topics: Collection<TopicData>) {
        _topicData = mutableListOf()
        topics.forEach {
            it -> _topicData.add(it)
        }
    }

    companion object {
        const val NO_STORED_URL = "QuizDroid.TopicRepository.ERROR_NO_STORED_URL_FOUND"

        const val TAG = "TopicRepository"
    }

    init {
//        try {
//            val ext_storage = Environment.getExternalStorageDirectory().absolutePath
//            val json = File("$ext_storage/Android/data/edu.washington.ieand.quizdroid/questions.json").readText()
//            val collectionType: Type = object : TypeToken<Collection<TopicData>>() { }.type
//            val topicsFromStorage: Collection<TopicData> = Gson().fromJson(json, collectionType)
//
//            buildTopics(topicsFromStorage)
//        }
//        catch(e: Exception) {
//            Log.e(TAG, e.toString())
//        }
    }

//    init {
//        val topic1 = Topic("Math",
//                "A series of questions that will test your mathematical intuition.",
//                "Trivia and tricky equations.")
//        topic1.addQuestion(Quiz(
//                "Sine, cosine, and what?",
//                "Quotient",
//                "Tangent",
//                "Agent",
//                "Potient",
//                2
//                ))
//        topic1.addQuestion(Quiz(
//                "What is 36 times 7?",
//                "256",
//                "264",
//                "246",
//                "252",
//                4
//        ))
//        topic1.addQuestion(Quiz(
//                "What does (9 + 9 / 3) resolve to?",
//                "6",
//                "12",
//                "9",
//                "15",
//                2
//        ))
//        topic1.addQuestion(Quiz(
//                "What is two to the power of eight?",
//                "128",
//                "256",
//                "512",
//                "1024",
//                2
//        ))
//        val topic2 = Topic("Physics",
//                "How well do you and gravity get along? Let's find out!",
//                "Theories, laws & Physics history")
//        topic2.addQuestion(Quiz(
//                "What was Newton's first law?",
//                "The law of motion",
//                "The law of gravity",
//                "The law of movement",
//                "The law of order",
//                2
//        ))
//        topic2.addQuestion(Quiz(
//                "What is the force that opposes the relative motion of two bodies that are in contact?",
//                "Normal Force",
//                "Magnetism",
//                "Friction",
//                "Gravity",
//                3
//        ))
//        topic2.addQuestion(Quiz(
//                "The discovery of which law/theory provoked the surprised cry \"Euraka\"?",
//                "The theory of gravity",
//                "Newton's 4th Law",
//                "The theory of relativity",
//                "The Archimedes Principle",
//                4
//        ))
//        topic2.addQuestion(Quiz(
//                "Magnets — How do the work?",
//                "Magic",
//                "The interactions between electrons of opposing charge",
//                "Friction",
//                "Electricity",
//                2
//        ))
//        val topic3 = Topic("Marvel Super Heroes",
//                "Feel like you could assemble all the Avengers? Let's see what kind of Nick Fury you make.",
//                "MCU & Marvel Comics trivia")
//        topic3.addQuestion(Quiz(
//                "What powers Iron Man's suit?",
//                "He was bit by a robot",
//                "An Arc Reactor",
//                "A rare Unobtainium core",
//                "Pure Russian Titanium",
//                2
//        ))
//        topic3.addQuestion(Quiz(
//                "In the comics, who else can lift Thor's hammer?",
//                "Wade Wilson",
//                "Miles Morales",
//                "Stephen Strange",
//                "Natasha Romanoff",
//                4
//        ))
//        topic3.addQuestion(Quiz(
//                "In the comics, how many Infinity Gems are there?",
//                "4",
//                "5",
//                "6",
//                "7",
//                3
//        ))
//        topic3.addQuestion(Quiz(
//                "How many Marvel movies has Stan Lee had a cameo in?",
//                "44",
//                "37",
//                "29",
//                "53",
//                1
//        ))
//        val topic4 = Topic("Greek Mythology",
//                "How well do you know the classic of Greece and Rome? This category will stress your knowledge of the ancient world.",
//                "Tales of Gods & Mortals")
//        topic4.addQuestion(Quiz(
//                "What was Odysseus' wife's name?",
//                "Persephone",
//                "Penelope",
//                "Circe",
//                "Thetis",
//                2
//        ))
//        topic4.addQuestion(Quiz(
//                "Which mythical beast did Theseus of Athens slay?",
//                "Medusa",
//                "Scylla",
//                "Hydra",
//                "Minotaur",
//                4
//        ))
//        topic4.addQuestion(Quiz(
//                "Who was the father of Kronos?",
//                "Zeus",
//                "Ouranos",
//                "Ares",
//                "Gaia",
//                2
//        ))
//        topic4.addQuestion(Quiz(
//                "What was the \"Works and Days\"?",
//                "A story of the world's creation",
//                "A Roman recounting of Hercules' deeds",
//                "An all-year farming advice guidebook",
//                "A calendar for Athenian statesmen",
//                3
//        ))
//
//        _topics.add(topic1)
//        _topics.add(topic2)
//        _topics.add(topic3)
//        _topics.add(topic4)
//    }
}