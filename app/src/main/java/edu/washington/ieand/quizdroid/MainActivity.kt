package edu.washington.ieand.quizdroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    private val instance = QuizApp.getSingletonInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listOfTopics = instance.repository.getTopics()
        val listOfTopicNames: List<String> = listOfTopics.map { it ->
            it.topicTitle
        }

        val lv: ListView = findViewById(R.id.listView_topics)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfTopicNames)
        lv.adapter = adapter

        lv.setOnItemClickListener { _, _, position, _ ->
            val currListView: ListView = findViewById(R.id.listView_topics)
            val item = currListView.getItemAtPosition(position)

            val intent = Intent(this, ControllerActivity::class.java)
                    .putExtra(ControllerActivity.TOPIC_NAME, item.toString())


            startActivity(intent)
        }
    }

    companion object {
        const val TAG: String = "ActivityMain"
    }
}


