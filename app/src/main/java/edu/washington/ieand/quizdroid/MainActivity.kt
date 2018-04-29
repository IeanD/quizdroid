package edu.washington.ieand.quizdroid

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val topics = listOf("Math", "Physics", "Marvel Super Heroes", "Greek Mythology")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var lv: ListView = findViewById(R.id.listView_topics)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics)
        lv.adapter = adapter

        lv.setOnItemClickListener { _, _, position, id ->
            var currListView: ListView = findViewById(R.id.listView_topics)
            var item = currListView.getItemAtPosition(position)

//            val intent = Intent(this, SummaryActivity::class.java)
//                    .putExtra(SummaryActivity.TOPIC, item.toString())
//                    .putExtra(SummaryActivity.DESCRIPTION, item.toString())
//                    .putExtra(SummaryActivity.TOPIC_NUM, position)
            val intent = Intent(this, ControllerActivity::class.java)
                    .putExtra(ControllerActivity.TOPIC_NAME, item.toString())

            startActivity(intent)
        }
    }

    companion object {
        const val TAG: String = "ActivityMain"
    }
}


