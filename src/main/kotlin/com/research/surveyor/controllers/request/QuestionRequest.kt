package com.research.surveyor.controllers.request

data class QuestionRequest(
    val id: Long = 0,
    val questionValue: String,
    val questionnaireId: Long,
)
