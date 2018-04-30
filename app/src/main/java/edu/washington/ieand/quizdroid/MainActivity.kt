package edu.washington.ieand.quizdroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.SimpleAdapter

class MainActivity : AppCompatActivity() {

    private val instance = QuizApp.getSingletonInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listOfTopics = instance.repository.getTopics()
        val listOfTopicNames = listOfTopics.map { it ->
            it.topicTitle
        }
        val listOfTopicNamesToDescriptions = listOfTopics.map { it ->
            arrayOf(it.topicTitle, it.topicShortDescription)
        }
        val list: List<HashMap<String, String>> = listOfTopicNamesToDescriptions.map { it ->
            val item: HashMap<String, String> = HashMap()
            item["line1"] = it[0]
            item["line2"] = it[1]

            item
        }

        val lv: ListView = findViewById(R.id.listView_topics)
        val simpleAdapter = SimpleAdapter(this,
                list, R.layout.two_line_list_item, arrayOf("line1", "line2"),
                intArrayOf(R.id.line_a, R.id.line_b))
        lv.adapter = simpleAdapter

        lv.setOnItemClickListener { _, _, position, _ ->
            val item = listOfTopicNames[position]

            val intent = Intent(this, ControllerActivity::class.java)
                    .putExtra(ControllerActivity.TOPIC_NAME, item)


            startActivity(intent)
        }
    }

    companion object {
        const val TAG: String = "ActivityMain"
    }
}


