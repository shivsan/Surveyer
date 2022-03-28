package com.research.surveyor.models

data class SurveyAnswers(
    val id: Int = 0,
    val questionnaireId: Long,
    val answers: List<Answer>
)

data class Answer(
    val questionId: Long,
    val answerOptionId: Long
)
