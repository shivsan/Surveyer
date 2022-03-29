package com.research.surveyor.controllers.request

data class SurveyAnswersDto(
    val questionnaireId: Long,
    val answers: List<Answer>,
)

data class Answer(
    val questionId: Long,
    val answerOptionId: Long
)
