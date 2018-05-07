package edu.washington.ieand.quizdroid

interface ITopicRepository {
    fun getTopic(topicName: String): TopicData
    fun getTopics(): List<TopicData>
    fun getDefaultUrl(): String
}