package edu.washington.ieand.quizdroid

interface ITopicRepository {
    fun getTopic(topicName: String): Topic
    fun getTopics(): List<Topic>
}