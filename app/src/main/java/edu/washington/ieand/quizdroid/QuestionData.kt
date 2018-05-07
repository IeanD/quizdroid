package edu.washington.ieand.quizdroid

data class QuestionData(val text: String?, val answer: Int = -1, val answers: Array<String>?) {

    fun correctAnswerToString(): String {
        return when(answer) {
            1 -> answers?.get(0) as String
            2 -> answers?.get(1) as String
            3 -> answers?.get(2) as String
            4 -> answers?.get(3) as String
            else -> {
                "NO_CORRECT_ANSWER"
            }
        }
    }
}