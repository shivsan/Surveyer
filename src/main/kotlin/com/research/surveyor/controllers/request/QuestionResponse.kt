package com.research.surveyor.controllers.request

data class QuestionResponse(
    val id: Long = 0,
    val questionValue: String,
    val questionnaireId: Long,
    val options: List<AnswerOptionResponse>,
)

data class AnswerOptionResponse(
    val id: Long = 0,
    val optionIndex: String,
    val option: String
)
